package com.compendium.hyrule.adapter

import androidx.recyclerview.widget.DiffUtil
import com.compendium.hyrule.model.CategoryItem

class SearchItemDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        val result = (oldItem.id == newItem.id && oldItem.imageUrl == newItem.imageUrl)
        return result
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
        val result = (oldItem.name == newItem.name && oldItem.category == newItem.category)
        return result
    }
}