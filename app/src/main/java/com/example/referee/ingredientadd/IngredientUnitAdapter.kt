package com.example.referee.ingredientadd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.databinding.ItemIngredientsUnitBinding

class IngredientUnitAdapter(private val items: Array<String>) :
    RecyclerView.Adapter<IngredientUnitAdapter.IngredientUnitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientUnitViewHolder {
        return IngredientUnitViewHolder(
            ItemIngredientsUnitBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent,false )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IngredientUnitViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class IngredientUnitViewHolder(private val binding: ItemIngredientsUnitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.unit = items[position]
        }
    }
}