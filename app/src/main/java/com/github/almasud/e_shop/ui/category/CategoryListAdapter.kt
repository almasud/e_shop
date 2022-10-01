package com.github.almasud.e_shop.ui.category

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.databinding.ItemCategoryBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.util.ImageUtil
import java.util.*

class CategoryListAdapter :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(object :
        DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }) {

    private var onCategoryClicked: ((Category, View) -> Unit)? = null
    private var rowIndex: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutBinding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)

        holder.updateUI(category, position)
    }

    inner class CategoryViewHolder(
        private val layoutBinding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun updateUI(category: Category, position: Int) {
            Log.i(TAG, "updateUI: category: ${category.enName} and position: $position")

            layoutBinding.tvCatName.text = category.enName?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            ImageUtil.setImageLinkWithTextView(
                layoutBinding.root.context,
                category.image?.url ?: "",
                category.enName,
                layoutBinding.ivCatIcon,
                layoutBinding.tvCatIcon,
                null
            )

            // Set item click listener
            layoutBinding.layoutCatButton.setOnClickListener { view ->
                // For selected highlight color
                rowIndex = position
                notifyDataSetChanged()
                // Set item click listener callback
                onCategoryClicked?.let { it(category, view) }
            }

            // For selected highlight color
            if (rowIndex == position) {
                layoutBinding.layoutCategory.background =
                    ContextCompat.getDrawable(
                        layoutBinding.root.context,
                        R.drawable.border_category_bottom_selected
                    )
                layoutBinding.ivCatIcon.setColorFilter(
                    ContextCompat.getColor(layoutBinding.root.context, R.color.select_category),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                layoutBinding.tvCatIcon.setTextColor(
                    ContextCompat.getColor(
                        layoutBinding.root.context,
                        R.color.select_category
                    )
                )
                layoutBinding.tvCatName.setTextColor(
                    ContextCompat.getColor(
                        layoutBinding.root.context,
                        R.color.select_category
                    )
                )
            } else {
                layoutBinding.layoutCategory.background =
                    ContextCompat.getDrawable(
                        layoutBinding.root.context,
                        R.drawable.border_category_bottom
                    )
                layoutBinding.ivCatIcon.setColorFilter(
                    ContextCompat.getColor(layoutBinding.root.context, R.color.icon),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                layoutBinding.tvCatIcon.setTextColor(
                    ContextCompat.getColor(
                        layoutBinding.root.context,
                        R.color.icon
                    )
                )
                layoutBinding.tvCatName.setTextColor(
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
    fun setOnCategoryClickListener(listener: (Category, View) -> Unit) {
        onCategoryClicked = listener
    }

    companion object {
        private const val TAG = "CategoryListAdapter"
    }

}