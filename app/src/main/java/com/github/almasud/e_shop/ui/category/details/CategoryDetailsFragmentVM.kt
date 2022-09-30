package com.github.almasud.e_shop.ui.category.details

import android.annotation.SuppressLint
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
class CategoryDetailsFragmentVM @Inject constructor(
    private val categoryRepo: CategoryRepo,
    application: Application
) : AndroidViewModel(application) {
    private val _expandableCategoriesByParentIdStateFlow =
        MutableStateFlow<NetworkResult<CategoryListByParentIdQuery.Data>>(NetworkResult.Loading())
    private val _subCategoriesByParentIdStateFlow =
        MutableStateFlow<NetworkResult<CategoryListByParentIdQuery.Data>>(NetworkResult.Loading())

    val expandableCategoriesByParentId: Flow<NetworkResult<CategoryListByParentIdQuery.Data>> =
        _expandableCategoriesByParentIdStateFlow
    val subCategoriesByParentId: Flow<NetworkResult<CategoryListByParentIdQuery.Data>> =
        _subCategoriesByParentIdStateFlow

    @SuppressLint("LongLogTag")
    fun loadExpandableCategoriesByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadExpandableCategoriesByParentId: is called for parentId: $parentId")
        _expandableCategoriesByParentIdStateFlow.update {
            categoryRepo.getCategoriesByParentIdFromApi(parentId)
        }
    }

    @SuppressLint("LongLogTag")
    fun loadSubCategoriesByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadSubCategoriesByParentId: is called for parentId: $parentId")
        _subCategoriesByParentIdStateFlow.update {
            categoryRepo.getCategoriesByParentIdFromApi(parentId)
        }
    }

    companion object {
        private const val TAG = "CategoryDetailsFragmentVM"
    }
}