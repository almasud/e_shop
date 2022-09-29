package com.github.almasud.e_shop.ui.category.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.databinding.ItemCategoryExpandableBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.category.details.sub_category.SubCategoryListAdapter


class CategoryListExpandableAdapter :
    ListAdapter<Category, CategoryListExpandableAdapter.CategoryExpandableViewHolder>(object :
        DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }) {

    private var onExpandableCatExpanded: ((Category, View, SubCategoryListAdapter) -> Unit)? = null

    // An object of RecyclerView.RecycledViewPool　is created to share the Views　between
    // the child and　the parent RecyclerViews
    private val recycledViewPool = RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryExpandableViewHolder {
        val layoutBinding = ItemCategoryExpandableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryExpandableViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: CategoryExpandableViewHolder, position: Int) {
        val category = getItem(position)
        // Update the view
        holder.updateUI(category)
    }

    inner class CategoryExpandableViewHolder(
        private val layoutBinding: ItemCategoryExpandableBinding
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        fun updateUI(category: Category) {
            Log.i(TAG, "updateUI: is called")
            layoutBinding.tvCategory.text = category.enName

            // Set item click listener
            layoutBinding.root.setOnClickListener { view ->
                if (layoutBinding.rvSubCategory.visibility == View.GONE) {
                    Log.i(TAG, "updateUI: setOnClickListener: rvSubCategory is invisible")
                    layoutBinding.rvSubCategory.visibility = View.VISIBLE
                    layoutBinding.imageCatExpandable.setImageResource(R.drawable.ic_keyboard_arrow_up)

                    // Configure for nested recycler view
                    layoutBinding.rvSubCategory.layoutManager = GridLayoutManager(
                        layoutBinding.root.context, 3, GridLayoutManager.HORIZONTAL, false
                    )
                    val subCategoryListAdapter = SubCategoryListAdapter()
                    layoutBinding.rvSubCategory.adapter = subCategoryListAdapter
                    layoutBinding.rvSubCategory.setRecycledViewPool(recycledViewPool)

                    // Set the item click listener callback
                    onExpandableCatExpanded?.let { it(category, view, subCategoryListAdapter) }
                } else {
                    Log.i(TAG, "updateUI: setOnClickListener: rvSubCategory is visible")

                    layoutBinding.rvSubCategory.visibility = View.GONE
                    layoutBinding.imageCatExpandable.setImageResource(R.drawable.ic_keyboard_arrow_down)
                }
            }
        }
    }

    /**
     * Set the listener of on [Category] clicked
     */
    fun setOnExpandableCatExpandListener(listener: (Category, View, SubCategoryListAdapter) -> Unit) {
        onExpandableCatExpanded = listener
    }

    companion object {
        private const val TAG = "CategoryListExpandableA"
    }

}