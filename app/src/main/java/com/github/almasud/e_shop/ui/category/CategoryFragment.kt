package com.github.almasud.e_shop.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.exception.ApolloException
import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.data.api.ApiClient
import com.github.almasud.e_shop.databinding.FragmentCategoryBinding
import com.github.almasud.e_shop.domain.model.Category


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val categoryListAdapter = CategoryListAdapter()
    private var categories: MutableList<Category> = mutableListOf()

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

        categoryListAdapter.setOnCategoryClicked { category, view ->
            Log.i(TAG, "onCreateView: setOnCategoryClicked: is called")
            displayDetails(category)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        categories.clear()
        lifecycleScope.launchWhenResumed {
            val response = try {
                ApiClient.apolloClient.query(
                    CategoryListByParentIdQuery(
                        parentCategoryUid = "root",
                        pagination = 100,
                        skip = 0
                    )
                ).execute()
            } catch (e: ApolloException) {
                Log.e(TAG, "onStart: exception: " + e.message)
                return@launchWhenResumed
            }

            val fetchCategories = response.data?.getCategories
            Log.i(TAG, "onStart: fetchCategories: $fetchCategories")
            fetchCategories?.result?.categories?.forEach { category ->
                categories.add(Category.toCategory(category))
            }
            // Finally submit the categories
            categoryListAdapter.submitList(categories)
            categoryListAdapter.notifyDataSetChanged()
        }
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