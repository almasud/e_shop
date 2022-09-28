package com.github.almasud.e_shop.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.almasud.e_shop.databinding.ItemCategoryExpandableBinding
import com.github.almasud.e_shop.domain.model.Category


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

    private var onCategoryClicked: ((Category, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryExpandableViewHolder {
        val layoutBinding = ItemCategoryExpandableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryExpandableViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: CategoryExpandableViewHolder, position: Int) {
        val category = getItem(position)

        holder.updateUI(category)
    }

    inner class CategoryExpandableViewHolder(
        private val layoutBinding: ItemCategoryExpandableBinding
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        fun updateUI(category: Category) {
            layoutBinding.tvCategory.text = category.enName

            // Set item click listener callback
            layoutBinding.root.setOnClickListener { view ->
                onCategoryClicked?.let { it(category, view) }
            }
        }
    }

    /**
     * Set the listener of on [Category] clicked
     */
    fun setOnCategoryClicked(listener: (Category, View) -> Unit) {
        onCategoryClicked = listener
    }

}