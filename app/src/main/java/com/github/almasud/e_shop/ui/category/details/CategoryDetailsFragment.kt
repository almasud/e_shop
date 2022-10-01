package com.github.almasud.e_shop.ui.category.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.almasud.e_shop.data.api.NetworkResult
import com.github.almasud.e_shop.databinding.FragmentCategoryDetailsBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.category.details.sub_category.SubCategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val categoryListAdapter = CategoryListExpandableAdapter()
    private var categories: MutableList<Category> = mutableListOf()
    private var subCategories: MutableList<Category> = mutableListOf()
    private val viewModel: CategoryDetailsFragmentVM by viewModels()
    private lateinit var subCatAdapter: SubCategoryListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        binding.rvCatExpandable.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.rvCatExpandable.adapter = categoryListAdapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For expandable categories
        lifecycleScope.launch {
            viewModel.expandableCategoriesByParentId.collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        Log.i(
                            TAG,
                            "onViewCreated: expandableCategoriesByParentId data is loading..."
                        )
                    }
                    is NetworkResult.Success -> {
                        val fetchCategories = networkResult.data.getCategories?.result?.categories
                        Log.i(
                            TAG,
                            "onViewCreated: expandableCategoriesByParentId: fetchCategories: $fetchCategories"
                        )

                        categories.clear()
                        fetchCategories?.forEach { category ->
                            categories.add(Category.toCategory(category))
                        }
                        // Finally submit the categories
                        categoryListAdapter.submitList(categories)
                        categoryListAdapter.notifyDataSetChanged()

                        categoryListAdapter.setOnExpandableCatExpandListener { selectCategory, _, subCatAdapter ->
                            Log.i(
                                TAG,
                                "setOnExpandableCatClicked: category: ${selectCategory.enName} and uid: ${selectCategory.uid}"
                            )
                            this@CategoryDetailsFragment.subCatAdapter = subCatAdapter

                            // Load the sub categories
                            viewModel.loadSubCategoriesByParentId(selectCategory.uid!!)
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.e(
                            TAG,
                            "onViewCreated categoriesByParentId: error: ${networkResult.message}"
                        )
                    }
                    is NetworkResult.Exception -> {
                        Log.e(
                            TAG,
                            "onViewCreated: categoriesByParentId: error: ${networkResult.e.message}",
                            networkResult.e
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            // For sub categories
            viewModel.subCategoriesByParentId.collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        Log.i(TAG, "onViewCreated: subCategoriesByParentId data is loading...")
                    }
                    is NetworkResult.Success -> {
                        val fetchedSubCategories =
                            networkResult.data.getCategories?.result?.categories
                        Log.i(
                            TAG,
                            "onViewCreated: subCategoriesByParentId: fetchedSubCategories: $fetchedSubCategories"
                        )

                        subCategories.clear()
                        fetchedSubCategories?.forEach { category ->
                            subCategories.add(Category.toCategory(category))
                        }
                        // Finally submit the subCategories
                        if (this@CategoryDetailsFragment::subCatAdapter.isInitialized) {
                            subCatAdapter.submitList(subCategories)
                            subCatAdapter.notifyDataSetChanged()
                        }

                    }
                    is NetworkResult.Error -> {
                        Log.e(
                            TAG,
                            "onViewCreated categoriesByParentId: error: ${networkResult.message}"
                        )
                    }
                    is NetworkResult.Exception -> {
                        Log.e(
                            TAG,
                            "onViewCreated: categoriesByParentId: error: ${networkResult.e.message}",
                            networkResult.e
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayExpandableCategory(category: Category) {
        Log.i(
            TAG,
            "displayExpandableCategory: is called for category: ${category.enName} and uid: ${category.uid}"
        )

        // Load the expandable categories
        viewModel.loadExpandableCategoriesByParentId(category.uid!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CategoryDetailsFragment"
    }
}