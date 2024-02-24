package com.example.referee.common

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.referee.ingredientadd.model.IngredientCategoryType

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("glideBitmap")
    fun setImage(view: ImageView, bitmap: Bitmap? = null) {
        bitmap?.let {
            Glide
                .with(view.context)
                .load(bitmap)
                .thumbnail(0.1f)
                .skipMemoryCache(true)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("ingCategoryType")
    fun setCategoryIcon(view: TextView, type: IngredientCategoryType) {
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