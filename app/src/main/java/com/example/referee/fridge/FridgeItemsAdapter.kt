package com.example.referee.fridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemFridgeIngredientBinding
import com.example.referee.fridge.model.FridgeIngredientEntity

class FridgeItemsAdapter :
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

    inner class FridgeItemViewHolder(private val binding: ItemFridgeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.item = getItem(position)
        }
    }

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
}