package com.example.referee.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemRecipeBinding
import com.example.referee.recipe.model.RecipeEntity

class RecipeAdapter(private val onItemClick: ((position: Int) -> Unit)? = null) :
    BaseDiffUtilRecyclerAdapter<RecipeEntity, RecipeAdapter.RecipeViewHolder>(
        object : DiffUtil.ItemCallback<RecipeEntity>() {
            override fun areItemsTheSame(
                oldItem: RecipeEntity,
                newItem: RecipeEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RecipeEntity,
                newItem: RecipeEntity
            ): Boolean {
                return oldItem == newItem
            }
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getRecipeNumber(position: Int):Int {
        return getItem(position).recipe_no
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.recipe = getItem(position)
            binding.root.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }
}