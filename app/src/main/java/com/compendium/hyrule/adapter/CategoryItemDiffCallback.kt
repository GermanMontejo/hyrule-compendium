package com.compendium.hyrule.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.compendium.hyrule.model.CarouselCategory

class CategoryItemDiffCallback: ItemCallback<CarouselCategory>() {
    override fun areItemsTheSame(oldItem: CarouselCategory, newItem: CarouselCategory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarouselCategory, newItem: CarouselCategory): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
    }
}