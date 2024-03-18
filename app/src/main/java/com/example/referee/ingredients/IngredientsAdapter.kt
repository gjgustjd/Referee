package com.example.referee.ingredients

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.referee.R
import com.example.referee.common.Logger
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemIngredientBinding
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredients.model.IngredientsSelectableItem

class IngredientsAdapter(
    private val editFun: (item: IngredientEntity, sharedView: View) -> Unit,
    private val onItemChangeCompleted: (() -> Unit)? = null
) : BaseDiffUtilRecyclerAdapter<IngredientsSelectableItem, IngredientsAdapter.IngredientViewHolder>(
     object : DiffUtil.ItemCallback<IngredientsSelectableItem>() {
        override fun areItemsTheSame(
            oldItem: IngredientsSelectableItem,
            newItem: IngredientsSelectableItem
        ): Boolean {
            return oldItem.entity.id == newItem.entity.id
        }

        override fun areContentsTheSame(
            oldItem: IngredientsSelectableItem,
            newItem: IngredientsSelectableItem
        ): Boolean {
            return oldItem.entity == newItem.entity
        }
    }
) {
    private var updatedPosition: Int? = null
    var isDeleteMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun getItems() = currentList.map { it.entity }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).entity.id
    }

    fun getSelectedItem(): List<IngredientEntity> {
        return currentList.filter { it.isSelected }.map { it.entity }
    }

    fun submitList(list: List<IngredientsSelectableItem>, updatedPosition: Int? = null) {
        this.updatedPosition = updatedPosition
        super.submitList(list)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<IngredientsSelectableItem>,
        currentList: MutableList<IngredientsSelectableItem>
    ) {
        Logger.i()
        updatedPosition?.let {
            if (previousList == currentList) {
                notifyItemChanged(it)
            }
        }
        onItemChangeCompleted?.invoke()
    }

    inner class IngredientViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Logger.i("position:$position")
            if(position == updatedPosition) {
                binding.ivThumbnail.transitionName = "ingredientImage"
                binding.isResumeTranstion = true
                updatedPosition = null
                Logger.i("updatedPosition:$position")
            }

            val item = getItem(position)
            binding.item = item.entity

            binding.root.setOnLongClickListener {
                Log.i("EditTest","longClicked")
                editFun(item.entity,binding.ivThumbnail)
                true
            }

            item.entity.imageBitmap ?: run {
                val thumbnail = binding.ivThumbnail
                val context = thumbnail.context
                item.entity.photoName?.let {
                    Glide.with(context)
                        .clear(thumbnail)
                }
            }

            with(binding.cbIsDelete) {
                setOnCheckedChangeListener { _, isChecked ->
                    getItem(position).isSelected = isChecked
                    Log.i("DeleteTest",isChecked.toString())
                }

                val anim: Animation
                visibility = if (isDeleteMode) {
                    anim = AnimationUtils.loadAnimation(context, R.anim.scale_up)
                    View.VISIBLE
                } else {
                    isChecked = false
                    anim = AnimationUtils.loadAnimation(context, R.anim.scale_down)
                    View.GONE
                }

                startAnimation(anim)
            }
        }
    }
}