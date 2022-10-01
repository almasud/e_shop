package com.github.almasud.e_shop.ui.category.details

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.almasud.e_shop.domain.model.entity.Category
import com.github.almasud.e_shop.domain.repo.CategoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsFragmentVM @Inject constructor(
    private val categoryRepo: CategoryRepo,
    application: Application
) : AndroidViewModel(application) {
    private val expandableCategoryParentId = MutableStateFlow("Initial Value")
    private val subCategoryParentId = MutableStateFlow("Initial Value")

    @SuppressLint("LongLogTag")
    @OptIn(ExperimentalCoroutinesApi::class)
    val expandableCategoriesByParentId: Flow<PagingData<Category>> =
        expandableCategoryParentId.flatMapLatest { parentId ->
            Log.i(TAG, "categoriesByParentId: parentId: $parentId")
            categoryRepo.getCategoriesByParentId(parentId)
        }.cachedIn(viewModelScope)

    @SuppressLint("LongLogTag")
    @OptIn(ExperimentalCoroutinesApi::class)
    val subCategoriesByParentId: Flow<PagingData<Category>> =
        subCategoryParentId.flatMapLatest { parentId ->
            Log.i(TAG, "categoriesByParentId: parentId: $parentId")
            categoryRepo.getCategoriesByParentId(parentId)
        }.cachedIn(viewModelScope)

    @SuppressLint("LongLogTag")
    fun loadExpandableCategoriesByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadCategoryByParentId: parentId: $parentId")
        expandableCategoryParentId.update { parentId }
    }

    @SuppressLint("LongLogTag")
    fun loadSubCategoriesByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadCategoryByParentId: parentId: $parentId")
        subCategoryParentId.update { parentId }
    }

    companion object {
        private const val TAG = "CategoryDetailsFragmentVM"
    }
}