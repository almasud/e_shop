package com.github.almasud.e_shop.data.repo

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloHttpException
import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.data.db.AppDatabase
import com.github.almasud.e_shop.domain.model.entity.Category
import com.github.almasud.e_shop.domain.model.entity.RemoteKeys
import java.io.IOException

private const val STARTING_SKIP_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class CategoryRemoteMediator(
    private val parentCategoryId: String,
    private val apolloClient: ApolloClient,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Category>() {

    override suspend fun initialize(): InitializeAction {
        Log.i(TAG, "initialize: is called")

        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Category>): MediatorResult {
        Log.i(TAG, "load: is called")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(state.config.pageSize) ?: STARTING_SKIP_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = apolloClient.query(
                CategoryListByParentIdQuery(parentCategoryUid = parentCategoryId, pagination = state.config.pageSize, page)
            ).execute()
            Log.i(TAG, "load: apiResponse: $apiResponse")

            val categories = apiResponse.data?.getCategories?.result?.categories?.map { queryCategory ->
                Category.toCategory(queryCategory!!)
            }
            val endOfPaginationReached = categories?.isEmpty()!!
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao.clearRemoteKeysByCategoryId(parentCategoryId)
                    appDatabase.categoryDao.clearCategoriesByParentId(parentCategoryId)
                }
                val prevKey = if (page == STARTING_SKIP_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = categories.map {
                    RemoteKeys(categoryId = it.uid, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.remoteKeysDao.insertAllRemoteKey(keys)
                appDatabase.categoryDao.insertAllCategories(categories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: ApolloHttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Category>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { category ->
                // Get the remote keys of the last item retrieved
                appDatabase.remoteKeysDao.remoteKeysByCategoryId(category.uid)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Category>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { category ->
                // Get the remote keys of the first items retrieved
                appDatabase.remoteKeysDao.remoteKeysByCategoryId(category.uid)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Category>
    ): RemoteKeys? {
        Log.i(TAG, "getRemoteKeyClosestToCurrentPosition: is called")

        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uid?.let { categoryId ->
                appDatabase.remoteKeysDao.remoteKeysByCategoryId(categoryId)
            }
        }
    }

    companion object {
        private const val TAG = "CategoryRemoteMediator"
    }
}
