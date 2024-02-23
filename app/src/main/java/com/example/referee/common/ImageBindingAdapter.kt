package com.example.referee.common

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.referee.ingredientadd.model.IngredientCategoryType

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("glideSrc")
    fun setImage(view: ImageView, imageName: String? = null) {
        imageName?.let {
            val storage = view.context.cacheDir
            val path = "${storage}/$imageName"
            val bitmap = BitmapFactory.decodeFile(path)
            Glide
                .with(view.context)
                .load(bitmap)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("ingCategoryType")
    fun setCategoryIcon(view: TextView, type: IngredientCategoryType) {
        view.text = type.categoryName
        val drawable = ContextCompat.getDrawable(view.context, type.iconResourceId)
        view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    @JvmStatic
    @BindingAdapter("ingCategoryType")
    fun setCategoryIcon(view: TextView, categoryType: Int) {
        val type = IngredientCategoryType.values().firstOrNull {it.ordinal == categoryType}
        type?.let {
            val drawable = ContextCompat.getDrawable(view.context, it.iconResourceId)
            view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }
}