package com.example.referee.fridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemSearchIngredientBinding
import com.example.referee.network.model.mediawiki.List.Search

class SearchIngredientsAdapter(private val onItemClick: ((position: Int, title: String,pageid:Int,snippet:String) -> Unit)? = null) :
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
                binding.root.setOnClickListener {
                    onItemClick?.invoke(
                        adapterPosition,
                        item.title,
                        item.pageid,
                        item.snippet
                    )
                }
            }
    }
}