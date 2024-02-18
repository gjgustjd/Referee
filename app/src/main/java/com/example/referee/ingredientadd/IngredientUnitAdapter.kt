package com.example.referee.ingredientadd

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.databinding.ItemIngredientsUnitBinding

class IngredientUnitAdapter(val context: Context, private val items: Array<String>) :
    RecyclerView.Adapter<IngredientUnitAdapter.IngredientUnitViewHolder>() {

    private var currentSelectedPosition = 0

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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getSelectedItemString() = items[currentSelectedPosition]

    inner class IngredientUnitViewHolder(private val binding: ItemIngredientsUnitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.unit = items[position]
            binding.tvUnit.setOnClickListener {
                currentSelectedPosition = position
                notifyDataSetChanged()
            }
            if(position == currentSelectedPosition) {
                binding.tvUnit.background =
                    ResourcesCompat.getDrawable(context.resources,R.drawable.shape_ingredient_unit_background_enabled,null)

            } else {
                binding.tvUnit.background =
                    ResourcesCompat.getDrawable(context.resources,R.drawable.shape_ingredient_unit_background,null)
            }
        }
    }
}