package com.github.almasud.e_shop.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.databinding.FragmentCategoryBinding
import com.github.almasud.e_shop.domain.model.entity.Category
import com.github.almasud.e_shop.ui.category.details.CategoryDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val categoryListPagingAdapter = CategoryListPagingAdapter()

    //    private var categories: MutableList<Category> = mutableListOf()
    private val viewModel: CategoryFragmentVM by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        binding.rvCat.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.rvCat.adapter = categoryListPagingAdapter

        categoryListPagingAdapter.setOnCategoryClickListener { category, _ ->
            Log.i(TAG, "onCreateView: setOnCategoryClicked: is called")
            displayDetails(category)
        }

        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.categoriesByParentId.collectLatest { categoryPagingData ->
                categoryListPagingAdapter.submitData(categoryPagingData)
                categoryListPagingAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            categoryListPagingAdapter.loadStateFlow.collect { loadState ->
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Load the categories
        viewModel.loadCategoryByParentId("root")
    }

    private fun displayDetails(category: Category) {
        Log.i(TAG, "displayDetails: is called")

        val fragmentDualPaneDetails = childFragmentManager.findFragmentById(
            R.id.layoutCatDetails
        ) as CategoryDetailsFragment
        fragmentDualPaneDetails.displayExpandableCategory(category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CategoryFragment"
    }
}