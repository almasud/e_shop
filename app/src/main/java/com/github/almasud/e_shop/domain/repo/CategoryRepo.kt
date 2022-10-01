package com.github.almasud.e_shop.domain.repo

import androidx.paging.PagingData
import com.github.almasud.e_shop.domain.model.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {
    suspend fun getCategoriesByParentId(parentId: String)
            : Flow<PagingData<Category>>
}