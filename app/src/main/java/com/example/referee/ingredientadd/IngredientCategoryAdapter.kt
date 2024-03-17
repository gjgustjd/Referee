package com.example.referee.ingredientadd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.common.base.BaseRecyclerAdapter
import com.example.referee.databinding.ItemIngredientsCategoryBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType

class IngredientCategoryAdapter(
    val recyclerView: RecyclerView,
    items: Array<IngredientCategoryType>,
    private var currentSelectedPosition: Int = 0,
    private val onClick: ((type:IngredientCategoryType) -> Unit)? = null,
) :
    BaseRecyclerAdapter<IngredientCategoryType, IngredientCategoryAdapter.IngredientUnitViewHolder>(
        arrayListOf<IngredientCategoryType>().apply { addAll(items) }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientUnitViewHolder {
        return IngredientUnitViewHolder(
            ItemIngredientsCategoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent,false )
        )
    }

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

    fun getSelectedItem() = items[currentSelectedPosition]

    inner class IngredientUnitViewHolder(private val binding: ItemIngredientsCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = items[position]
            binding.category = item
            binding.tvCategory.setOnClickListener {
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
                onClick?.invoke(item)
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
            binding.tvCategory.background =
                ResourcesCompat.getDrawable(
                    recyclerView.context.resources,
                    resId,
                    null
                )
        }
    }
}