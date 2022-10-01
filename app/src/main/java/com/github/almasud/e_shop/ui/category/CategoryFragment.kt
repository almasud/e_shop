package com.github.almasud.e_shop.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.data.api.NetworkResult
import com.github.almasud.e_shop.databinding.FragmentCategoryBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.category.details.CategoryDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val categoryListAdapter = CategoryListAdapter()
    private var categories: MutableList<Category> = mutableListOf()
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
        binding.rvCat.adapter = categoryListAdapter

        categoryListAdapter.setOnCategoryClickListener { category, _ ->
            Log.i(TAG, "onCreateView: setOnCategoryClicked: is called")
            displayDetails(category)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.categoriesByParentId.collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        Log.i(TAG, "onViewCreated: categoriesByParentId data is loading...")
                    }
                    is NetworkResult.Success -> {
                        val fetchCategories = networkResult.data.getCategories?.result?.categories
                        Log.i(
                            TAG,
                            "onViewCreated: categoriesByParentId: fetchCategories: $fetchCategories"
                        )

                        categories.clear()
                        fetchCategories?.forEach { category ->
                            categories.add(Category.toCategory(category))
                        }
                        // Finally submit the categories
                        categoryListAdapter.submitList(categories)
                        categoryListAdapter.notifyDataSetChanged()
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