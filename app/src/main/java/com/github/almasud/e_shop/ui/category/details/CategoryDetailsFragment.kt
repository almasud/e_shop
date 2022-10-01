package com.github.almasud.e_shop.ui.category.details

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
import com.github.almasud.e_shop.databinding.FragmentCategoryDetailsBinding
import com.github.almasud.e_shop.domain.model.entity.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailsFragment : Fragment() {

    private var _binding: FragmentCategoryDetailsBinding? = null
    private val catExpandablePagingAdapter = CatExpandablePagingAdapter()
    private var categories: MutableList<Category> = mutableListOf()
    private var subCategories: MutableList<Category> = mutableListOf()
    private val viewModel: CategoryDetailsFragmentVM by viewModels()
//    private lateinit var subCatAdapter: SubCategoryListAdapter

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
        binding.rvCatExpandable.adapter = catExpandablePagingAdapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For expandable categories
        lifecycleScope.launch {
            viewModel.expandableCategoriesByParentId.collectLatest { categoryPagingData ->
                catExpandablePagingAdapter.submitData(categoryPagingData)
                catExpandablePagingAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            catExpandablePagingAdapter.loadStateFlow.collect { loadState ->
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

        catExpandablePagingAdapter.setOnExpandableCatExpandListener { selectCategory, _, subCatAdapter ->
            Log.i(
                TAG,
                "setOnExpandableCatClicked: category: ${selectCategory.enName} and uid: ${selectCategory.uid}"
            )

            lifecycleScope.launch {
                viewModel.subCategoriesByParentId.collectLatest { subCategoryPagingData ->
                    subCatAdapter.submitData(subCategoryPagingData)
                    subCatAdapter.notifyDataSetChanged()
                }
            }

            lifecycleScope.launch {
                subCatAdapter.loadStateFlow.collect { loadState ->
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

            // Load the sub categories
            viewModel.loadSubCategoriesByParentId(selectCategory.uid)
        }

//        lifecycleScope.launch {
//            viewModel.subCategoriesByParentId.collectLatest { subCategoryPagingData ->
//                if (this@CategoryDetailsFragment::subCatAdapter.isInitialized) {
//                    subCatAdapter.submitList(subCategories)
//                    subCatAdapter.notifyDataSetChanged()
//                }
//            }
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayExpandableCategory(category: Category) {
        Log.i(
            TAG,
            "displayExpandableCategory: is called for category: ${category.enName} and uid: ${category.uid}"
        )

        // Load the expandable categories
        viewModel.loadExpandableCategoriesByParentId(category.uid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CategoryDetailsFragment"
    }
}