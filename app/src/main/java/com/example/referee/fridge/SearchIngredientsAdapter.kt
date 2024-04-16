package com.example.referee.fridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemSearchIngredientBinding
import com.example.referee.network.model.mediawiki.List.Search

class SearchIngredientsAdapter :
    BaseDiffUtilRecyclerAdapter<Search, SearchIngredientsAdapter.SearchIngredientViewHolder>(
        object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(
                oldItem: Search,
                newItem: Search
            ): Boolean {
                return oldItem.pageid == newItem.pageid
            }

            override fun areContentsTheSame(
                oldItem: Search,
                newItem: Search
            ): Boolean {
                return oldItem == newItem
            }
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchIngredientViewHolder {
        val binding = ItemSearchIngredientBinding.inflate(LayoutInflater.from(parent.context))
        return SearchIngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchIngredientViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class SearchIngredientViewHolder(private val binding: ItemSearchIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(position:Int) {
                val item = getItem(position)
                binding.tvName.text = item.title
                binding.tvDesc.text = item.snippet
            }
    }
}