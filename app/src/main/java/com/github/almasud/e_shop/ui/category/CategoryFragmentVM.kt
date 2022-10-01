package com.github.almasud.e_shop.ui.category

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryFragmentVM @Inject constructor(
    private val categoryRepo: CategoryRepo,
    application: Application
) : AndroidViewModel(application) {
    private val parentIdMutableStateFlow = MutableStateFlow("Initial Value")

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesByParentId: Flow<PagingData<Category>> =
        parentIdMutableStateFlow.flatMapLatest { parentId ->
            Log.i(TAG, "categoriesByParentId: parentId: $parentId")
            categoryRepo.getCategoriesByParentId(parentId)
        }.cachedIn(viewModelScope)

    fun loadCategoryByParentId(parentId: String) = viewModelScope.launch {
        Log.i(TAG, "loadCategoryByParentId: parentId: $parentId")
        parentIdMutableStateFlow.update { parentId }
    }

    companion object {
        private const val TAG = "CategoryFragmentVM"
    }
}