package com.example.referee.common

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.referee.R
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredients.model.IngredientFragFABState
import com.example.referee.ingredients.model.IngredientsFABType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageName", "glideBitmap", "ingCategoryType","startAnimOnLoadFinished")
    fun setImageByName(
        view: ImageView,
        imageName: String? = null,
        bitmap: Bitmap? = null,
        categoryType: IngredientCategoryType? = null,
        startAnimOnLoadFinished: Boolean = false
    ) {
        val source = imageName?.let {
            val storage = RefereeApplication.instance.applicationContext.cacheDir
            File(storage, it)
        } ?: bitmap
        source?.let {
            Glide
                .with(view.context)
                .load(source)
                .thumbnail(0.3f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply {
                    categoryType?.let {
                        val padding =
                            view.context.resources.getDimension(R.dimen.ingredient_placeholder_inset)
                                .toInt()
                        val drawable =
                            ResourcesCompat.getDrawable(
                                view.context.resources,
                                it.iconResourceId,
                                null
                            )
                        val insetDrawable =
                            InsetDrawable(drawable, CommonUtil.pxToDp(view.context, padding))
                        placeholder(insetDrawable)
                    }
                    if(startAnimOnLoadFinished) {
                        listener(
                        object :RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                ((view.context) as Activity).startPostponedEnterTransition()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                ((view.context) as Activity).startPostponedEnterTransition()
                                Logger.i("")
                                return false
                            }
                        })
                    }
                }.into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageName", "glideBitmap", "ingCategoryType")
    fun setImageByName(
        view: ImageView,
        imageName: String? = null,
        bitmap: Bitmap? = null,
        categoryType: Int? = null
    ) {
        setImageByName(view, imageName, bitmap, categoryType?.let {
            IngredientCategoryType.fromInt(it)
        })
    }

    @JvmStatic
    @BindingAdapter("glideBitmap","ingCategoryType")
    fun setImage(view: ImageView, bitmap: Bitmap? = null, categoryType: Int? = null) {
        categoryType?.let {
            setImage(view, bitmap, IngredientCategoryType.fromInt(it))
        }
    }

    @JvmStatic
    @BindingAdapter("glideBitmap","ingCategoryType")
    fun setImage(view: ImageView, bitmap: Bitmap? = null, categoryType: IngredientCategoryType? = null) {
        Glide.with(view.context)
            .load(bitmap)
            .thumbnail(0.3f)
            .skipMemoryCache(true)
            .apply {
                val padding =
                    view.context.resources.getDimension(R.dimen.ingredient_placeholder_inset)
                        .toInt()
                categoryType?.let {
                    val drawable =
                        ResourcesCompat.getDrawable(view.context.resources, it.iconResourceId, null)
                    val insetDrawable =
                        InsetDrawable(drawable, CommonUtil.pxToDp(view.context, padding))
                    placeholder(insetDrawable)
                }
            }
            .into(view)
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
        IngredientCategoryType.fromInt(categoryType)?.let {
            setCategoryIcon(view, it)
        }
    }

    @JvmStatic
    @BindingAdapter("fabState","fabType")
    fun setFabIcon(view:FloatingActionButton,state:IngredientFragFABState,type:IngredientsFABType) {
        val resources = view.resources
        val resId = when (type) {
            IngredientsFABType.MAIN_FAB -> {
                when (state) {
                    IngredientFragFABState.None, IngredientFragFABState.SubMenu -> {
                        R.drawable.ic_add
                    }

                    else -> {
                        R.drawable.ic_undo
                    }
                }
            }

            IngredientsFABType.SUB_FIRST_FAB -> {
                when (state) {
                    IngredientFragFABState.None,IngredientFragFABState.SubMenu -> R.drawable.ic_trashbin
                    IngredientFragFABState.DeleteMenu -> R.drawable.ic_trash_check

                    else -> return
                }
            }

            IngredientsFABType.SUB_SECOND_FAB -> {
                R.drawable.ic_search
            }
        }

        val drawable = ResourcesCompat.getDrawable(resources, resId, null)
        view.setImageDrawable(drawable)
    }
}