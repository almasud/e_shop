package com.github.almasud.e_shop.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.data.api.NetworkResult
import com.github.almasud.e_shop.domain.repo.CategoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryFragmentVM @Inject constructor(
    private val categoryRepo: CategoryRepo,
    application: Application
) : AndroidViewModel(application) {
    private val _categoriesByParentId =
        MutableStateFlow<NetworkResult<CategoryListByParentIdQuery.Data>>(NetworkResult.Loading())
    val categoriesByParentId: Flow<NetworkResult<CategoryListByParentIdQuery.Data>> =
        _categoriesByParentId

    fun loadCategoryByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadCategoryByParentId: parentId: $parentId")
        _categoriesByParentId.update {
            categoryRepo.getCategoriesByParentIdFromApi(parentId)
        }
    }

    companion object {
        private const val TAG = "CategoryFragmentVM"
    }
}