package com.github.almasud.e_shop.ui.category.details.sub_category

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.almasud.e_shop.databinding.ItemSubCategoryBinding
import com.github.almasud.e_shop.domain.model.Category
import com.github.almasud.e_shop.ui.util.ImageUtil


class SubCategoryListAdapter :
    ListAdapter<Category, SubCategoryListAdapter.CategoryViewHolder>(object :
        DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }) {

    private var onCategoryClicked: ((Category, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutBinding = ItemSubCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CategoryViewHolder(layoutBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)

        holder.updateUI(category)
    }

    inner class CategoryViewHolder(
        private val layoutBinding: ItemSubCategoryBinding
    ) : RecyclerView.ViewHolder(layoutBinding.root) {

        fun updateUI(category: Category) {
            layoutBinding.tvSubCatName.text = category.enName
            ImageUtil.setImageLinkWithTextView(
                layoutBinding.root.context,
                category.image?.url ?: "",
                category.enName,
                layoutBinding.ivSubCatIcon,
                layoutBinding.tvSubCatIcon,
                null
            )

            // Set item click listener callback
            layoutBinding.layoutSubCatButton.setOnClickListener { view ->
                Log.i(TAG, "updateUI: is called")
                onCategoryClicked?.let { it(category, view) }
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
        private const val TAG = "SubCategoryListAdapter"
    }

}