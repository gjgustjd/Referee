package com.example.referee.fridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemFridgeIngredientBinding
import com.example.referee.fridge.model.FridgeIngredientEntity

class FridgeItemsAdapter(private val onItemClick: ((name: String) -> Unit)? = null) :
    BaseDiffUtilRecyclerAdapter<FridgeIngredientEntity, FridgeItemsAdapter.FridgeItemViewHolder>(
        object : DiffUtil.ItemCallback<FridgeIngredientEntity>() {
            override fun areItemsTheSame(
                oldItem: FridgeIngredientEntity,
                newItem: FridgeIngredientEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FridgeIngredientEntity,
                newItem: FridgeIngredientEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeItemViewHolder {
        return FridgeItemViewHolder(
            ItemFridgeIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FridgeItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onViewRecycled(holder: FridgeItemViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.binding.ivThumbnail)
    }

    inner class FridgeItemViewHolder(val binding: ItemFridgeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = getItem(position)
            binding.item = item
            binding.root.setOnClickListener {
                onItemClick?.invoke(item.name)
            }
        }
    }
}