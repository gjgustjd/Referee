package com.example.referee.ingredients

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.referee.R
import com.example.referee.databinding.ItemIngredientBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity

class IngredientsAdapter(
    private val items: List<IngredientEntity>,
    private val bindThumbFun: (imageName: String, position: Int) -> Unit
) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

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
        Log.i(
            "BindingTest onBindViewHolder ",
            "position:$position imageName:${items[position].photoName}"
        )
        holder.bind(position)
    }

    fun bindThumbnail(bitmap:Bitmap,position: Int) {
        Log.i(
            "BindingTest bindThumbnail",
            "position:$position imageName:${items[position].photoName}"
        )
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
        }
    }
}