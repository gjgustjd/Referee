package com.example.referee.common

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

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
}