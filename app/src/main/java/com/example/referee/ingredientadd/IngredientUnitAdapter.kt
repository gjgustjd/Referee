package com.example.referee.ingredientadd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.common.base.BaseRecyclerAdapter
import com.example.referee.databinding.ItemIngredientsUnitBinding

class IngredientUnitAdapter(
    private val recyclerView: RecyclerView,
    items: Array<String>,
    private var currentSelectedPosition: Int = 0
) :
    BaseRecyclerAdapter<String, IngredientUnitAdapter.IngredientUnitViewHolder>(
        arrayListOf<String>().apply { addAll(items) }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientUnitViewHolder {
        return IngredientUnitViewHolder(
            ItemIngredientsUnitBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IngredientUnitViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onBindViewHolder(
        holder: IngredientUnitViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.onEach {
            when (it) {
                PAYLOAD_SELECT_ITEM -> {
                    holder.setBackground()
                }
            }
        }.ifEmpty {
            holder.bind(position)
        }
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
                val toPosition = if (currentSelectedPosition < position) {
                    (position + 1).coerceAtMost(items.lastIndex)
                } else if (currentSelectedPosition == position) {
                    position
                } else {
                    (position - 1).coerceAtLeast(0)
                }

                recyclerView.smoothScrollToPosition(toPosition)
                val prevSelectedPosition = currentSelectedPosition
                currentSelectedPosition = position
                notifyItemChanged(prevSelectedPosition, PAYLOAD_SELECT_ITEM)
                notifyItemChanged(position, PAYLOAD_SELECT_ITEM)
            }

            setBackground()
        }

        fun setBackground() {
            val resId =
                if (adapterPosition == currentSelectedPosition) {
                    R.drawable.shape_ingredient_unit_background_enabled

                } else {
                    R.drawable.shape_ingredient_unit_background
                }
            binding.tvUnit.background =
                ResourcesCompat.getDrawable(
                    recyclerView.context.resources,
                    resId,
                    null
                )
        }
    }
}