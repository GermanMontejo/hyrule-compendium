package com.ebookfrenzy.hyrule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.hyrule.databinding.EntryCategoryBinding
import com.ebookfrenzy.hyrule.model.CarouselCategory

class CategoryAdapter: ListAdapter<CarouselCategory, CategoryAdapter.CategoryViewHolder>(CategoryItemDiffCallback()) {

    private var itemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = EntryCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: EntryCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(category: CarouselCategory) {
            binding.apply {
                imageViewCategory.setImageResource(category.imageResourceId)
                textViewCategory.text = category.name
                root.setOnClickListener {
                    itemClickListener?.let {
                        it(category.name)
                    }
                }
            }
        }
    }
}