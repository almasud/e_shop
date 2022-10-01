package com.github.almasud.e_shop.ui.category.details

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.databinding.ItemCategoryExpandableBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.category.details.sub_category.SubCategoryListAdapter
import java.util.*

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
    private var rowIndex = -1

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
        holder.updateUI(category, position)
    }

    inner class CategoryExpandableViewHolder(
        private val layoutBinding: ItemCategoryExpandableBinding
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        @SuppressLint("LongLogTag", "NotifyDataSetChanged")
        fun updateUI(category: Category, position: Int) {
            Log.i(TAG, "updateUI: is called: category: ${category.enName} and position: $position")
            layoutBinding.tvCategory.text = category.enName?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }

            // Set item click listener
            layoutBinding.root.setOnClickListener { view ->
                // For selected highlight color
                rowIndex = position
                notifyDataSetChanged()

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

            // For selected highlight color
            if (rowIndex == position) {
                layoutBinding.imageCatExpandable.setColorFilter(
                    ContextCompat.getColor(layoutBinding.root.context, R.color.select_category),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                layoutBinding.tvCategory.setTextColor(
                    ContextCompat.getColor(
                        layoutBinding.root.context,
                        R.color.select_category
                    )
                )
            } else {
                layoutBinding.imageCatExpandable.setColorFilter(
                    ContextCompat.getColor(layoutBinding.root.context, R.color.icon),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                layoutBinding.tvCategory.setTextColor(
                    ContextCompat.getColor(
                        layoutBinding.root.context,
                        R.color.icon
                    )
                )
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
        private const val TAG = "CategoryListExpandableAdapter"
    }

}