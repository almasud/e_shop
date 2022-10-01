package com.github.almasud.e_shop.data.repo

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.github.almasud.e_shop.data.db.AppDatabase
import com.github.almasud.e_shop.domain.model.entity.Category
import com.github.almasud.e_shop.domain.repo.CategoryRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val appDatabase: AppDatabase
) : CategoryRepo {
    override suspend fun getCategoriesByParentId(parentId: String)
            : Flow<PagingData<Category>> {
        Log.d(TAG, "New parentId: $parentId")
        val pagingSourceFactory = { appDatabase.categoryDao.getCategoriesByParentId(parentId) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CategoryRemoteMediator(
                parentCategoryId = parentId,
                apolloClient = apolloClient,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
        private const val TAG = "CategoryRepoImpl"
    }
}