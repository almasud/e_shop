package com.github.almasud.e_shop.domain.repo

import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.data.api.NetworkResult

interface CategoryRepo {
    suspend fun getCategoriesByParentIdFromApi(uid: String)
            : NetworkResult<CategoryListByParentIdQuery.Data>
}