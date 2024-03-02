package com.example.referee.ingredients

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.referee.R
import com.example.referee.databinding.ItemIngredientBinding
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredients.model.IngredientsSelectableItem

class IngredientsAdapter(
    private val items: MutableList<IngredientsSelectableItem>,
    private val editFun: (item: IngredientEntity) -> Unit,
    private val bindThumbFun: (imageName: String, position: Int) -> Unit
) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

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

    fun getItems() = items.map { it.entity }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long {
        return items[position].entity.id
    }

    fun bindThumbnail(bitmap:Bitmap,position: Int) {
        items[position].entity.imageBitmap = bitmap
        notifyItemChanged(position)
    }

    fun getSelectedItem(): List<IngredientEntity> {
        return items.filter { it.isSelected }.map { it.entity }
    }

    inner class IngredientViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = items[position]
            binding.item = item.entity

            binding.root.setOnLongClickListener {
                Log.i("EditTest","longClicked")
                editFun(item.entity)
                true
            }

            item.entity.imageBitmap ?: run {
                val thumbnail = binding.ivThumbnail
                val context = thumbnail.context
                item.entity.photoName?.let {
                    Glide.with(context)
                        .clear(thumbnail)
                    bindThumbFun(it, position)
                }
            }

            with(binding.cbIsDelete) {
                setOnCheckedChangeListener { _, isChecked ->
                    items[position].isSelected = isChecked
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