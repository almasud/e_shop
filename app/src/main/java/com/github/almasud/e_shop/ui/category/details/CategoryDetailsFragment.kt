package com.github.almasud.e_shop.ui.category.details

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
import com.github.almasud.e_shop.data.api.ApiClient
import com.github.almasud.e_shop.databinding.FragmentCategoryDetailsBinding
import com.github.almasud.e_shop.domain.model.Category


class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val categoryListAdapter = CategoryListExpandableAdapter()
    private var categories: MutableList<Category> = mutableListOf()
    private var subCategories: MutableList<Category> = mutableListOf()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayExpandableCategory(category: Category) {
        Log.i(TAG, "displayExpandableCategory: is called for category: $category")

        categories.clear()
        lifecycleScope.launchWhenResumed {
            val response = try {
                ApiClient.apolloClient.query(
                    CategoryListByParentIdQuery(parentCategoryUid = category.uid!!, pagination = 100, skip = 0)
                ).execute()
            } catch (e: ApolloException) {
                Log.e(TAG, "displayExpandableCategory: exception: " + e.message)
                return@launchWhenResumed
            }

            val fetchCategories = response.data?.getCategories
            Log.i(TAG, "displayExpandableCategory: fetchCategories: $fetchCategories")
            fetchCategories?.result?.categories?.forEach { category ->
                categories.add(Category.toCategory(category))
            }
            // Finally submit the categories
            categoryListAdapter.submitList(categories)
            categoryListAdapter.notifyDataSetChanged()

            categoryListAdapter.setOnExpandableCatExpandListener { selectCategory, _, subCatAdapter ->
                Log.i(TAG, "setOnExpandableCatClicked: category: $selectCategory")

                subCategories.clear()
                lifecycleScope.launchWhenResumed {
                    val subCategoryResponse = try {
                        ApiClient.apolloClient.query(
                            CategoryListByParentIdQuery(parentCategoryUid = selectCategory.uid!!, pagination = 100, skip = 0)
                        ).execute()
                    } catch (e: ApolloException) {
                        Log.e(TAG, "displayExpandableCategory: exception: " + e.message)
                        return@launchWhenResumed
                    }

                    val fetchedSubCategories = subCategoryResponse.data?.getCategories
                    Log.i(TAG, "displayExpandableCategory: fetchedSubCategories: $fetchedSubCategories")
                    fetchedSubCategories?.result?.categories?.forEach { category ->
                        subCategories.add(Category.toCategory(category))
                    }
                    // Finally submit the subCategories
                    subCatAdapter.submitList(subCategories)
                    subCatAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CategoryDetailsFragment"
    }
}