package com.github.almasud.e_shop.data.repo

import com.apollographql.apollo3.ApolloClient
import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.data.api.NetworkResult
import com.github.almasud.e_shop.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val apolloClient: ApolloClient
): CategoryRepo {
    override suspend fun getCategoriesByParentIdFromApi(uid: String)
    : NetworkResult<CategoryListByParentIdQuery.Data> =
        NetworkResult.handleGraphQLApi {
            apolloClient.query(
                CategoryListByParentIdQuery(parentCategoryUid = uid, pagination = 100, skip = 0)
            ).execute()
        }
}