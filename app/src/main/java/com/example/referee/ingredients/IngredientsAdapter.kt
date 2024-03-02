package com.example.referee.ingredients

import android.graphics.Bitmap
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

class IngredientsAdapter(
    private val items: List<IngredientEntity>,
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

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun bindThumbnail(bitmap:Bitmap,position: Int) {
        items[position].imageBitmap = bitmap
        notifyItemChanged(position)
    }

    inner class IngredientViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = items[position]
            binding.item = item

            item.imageBitmap ?: run {
                val thumbnail = binding.ivThumbnail
                val context = thumbnail.context
                item.photoName?.let {
                    Glide.with(context)
                        .clear(thumbnail)
                    bindThumbFun(it, position)
                }
            }

            with(binding.cbIsDelete) {
                val anim: Animation
                visibility = if (isDeleteMode) {
                    anim = AnimationUtils.loadAnimation(context, R.anim.scale_up)
                    View.VISIBLE
                } else {
                    anim = AnimationUtils.loadAnimation(context, R.anim.scale_down)
                    View.GONE
                }

                startAnimation(anim)
            }
        }
    }
}