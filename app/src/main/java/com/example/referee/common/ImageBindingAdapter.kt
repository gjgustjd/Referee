package com.example.referee.common

import android.graphics.Bitmap
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.referee.R
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredients.model.IngredientFragFABState
import com.example.referee.ingredients.model.IngredientsFABType
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("glideBitmap","ingCategoryType")
    fun setImage(view: ImageView, bitmap: Bitmap? = null, categoryType: Int? = null) {
        Glide
            .with(view.context)
            .load(bitmap)
            .thumbnail(0.1f)
            .skipMemoryCache(true)
            .apply {
                val padding =
                    view.context.resources.getDimension(R.dimen.ingredient_placeholder_inset)
                        .toInt()
                Log.i("paddingTest",padding.toString())
                val drawable = categoryType?.let {
                    IngredientCategoryType.fromInt(it)?.let { type ->
                        ResourcesCompat.getDrawable(view.context.resources, type.iconResourceId,null)
                    }
                }
                val insetDrawable =
                    InsetDrawable(drawable, CommonUtil.pxToDp(view.context, padding))
                placeholder(insetDrawable)
            }
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("glideBitmap","ingCategoryType")
    fun setImage(view: ImageView, bitmap: Bitmap? = null, categoryType: IngredientCategoryType? = null) {
        Glide.with(view.context)
            .load(bitmap)
            .thumbnail(0.1f)
            .skipMemoryCache(true)
            .apply {
                val padding =
                    view.context.resources.getDimension(R.dimen.ingredient_placeholder_inset)
                        .toInt()
                Log.i("paddingTest", padding.toString())
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
        IngredientCategoryType.fromInt(categoryType)?.let { type ->
            val drawable = ContextCompat.getDrawable(view.context, type.iconResourceId)
            view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    @JvmStatic
    @BindingAdapter("fabState","fabType")
    fun setFabIcon(view:FloatingActionButton,state:IngredientFragFABState,type:IngredientsFABType) {
        Log.i("FabTest","setFabIcon")
        Log.i("FabTest",type.toString())
        Log.i("FabTest",state.toString())
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