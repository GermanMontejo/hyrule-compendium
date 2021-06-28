package com.compendium.hyrule.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.compendium.hyrule.R
import com.compendium.hyrule.databinding.RecyclerItemBinding
import com.compendium.hyrule.model.CategoryItem
import com.compendium.hyrule.utils.Formatter

class SearchAdapter :
    ListAdapter<CategoryItem, SearchAdapter.SearchViewHolder>(SearchItemDiffCallback()) {
    private lateinit var binding: RecyclerItemBinding
    private lateinit var itemClickListener: (String) -> Unit
    fun setItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class SearchViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(entry: CategoryItem) {
            itemView.apply {
                val textViewName = findViewById<TextView>(R.id.textViewName)
                val imageCategory = findViewById<ImageView>(R.id.imageCategory)
                val textViewInfo1 = findViewById<TextView>(R.id.textViewInfo1)
                val textViewInfo2 = findViewById<TextView>(R.id.textViewInfo2)
                Glide.with(this).load(entry.imageUrl).into(imageCategory)
                val info1 =
                    Formatter.capitalizeFirstLetters(if (entry.info1.isBlank()) "N/A" else entry.info1)
                val info2 = Formatter.capitalizeFirstLetters(entry.info2)

                textViewName.text = Formatter.capitalizeFirstLetters(entry.name)
                textViewInfo1.text = info1
                textViewInfo2.text = info2
                textViewInfo1.ellipsize = TextUtils.TruncateAt.END
                textViewInfo2.ellipsize = TextUtils.TruncateAt.END

                setOnClickListener {
                    itemClickListener.let {
                        it(entry.name.replace("Name: ", ""))
                    }
                }
            }
        }
    }
}