package com.example.referee.common

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.referee.ingredientadd.model.IngredientCategoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("glideSrc")
    fun setImage(view: ImageView, imageName: String? = null) {
        view.findViewTreeLifecycleOwner()?.lifecycleScope?.launch(Dispatchers.IO) {
            val storage = view.context.cacheDir
            val path = "${storage}/$imageName"
            val bitmap = BitmapFactory.decodeFile(path)
            launch(Dispatchers.Main) {
                imageName?.let {
                    Glide
                        .with(view.context)
                        .load(bitmap)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view)
                }
            }
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