package com.example.referee.ingredients

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.referee.R
import com.example.referee.common.CommonUtil
import com.example.referee.common.Const
import com.example.referee.common.Logger
import com.example.referee.common.RefereeApplication
import com.example.referee.common.base.BaseDiffUtilRecyclerAdapter
import com.example.referee.databinding.ItemIngredientBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredients.model.IngredientsSelectableItem
import java.io.File

class IngredientsAdapter(
    private val editFun: (item: IngredientEntity, sharedView: View) -> Unit,
    private val onItemChangeCompleted: ((position:Int) -> Unit)? = null
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
        Logger.i("newUpdatedPosition:$updatedPosition")
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

            onItemChangeCompleted?.invoke(it)
        }
    }

    inner class IngredientViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Logger.i("updatePosition:$updatedPosition")
            Logger.i("position:$position")
            var restartTransition = false
            if(position == updatedPosition) {
                binding.ivThumbnail.transitionName = Const.TRANSITION_NAME_INGREDIENT_IMAGE
                restartTransition = true
                Logger.i("transitionName updated")
            }

            val item = getItem(position)
            binding.item = item.entity
            item.entity.category?.let { category ->
                IngredientCategoryType.fromInt(category)?.let {
                    bindImage(
                        binding.ivThumbnail,
                        item.entity.photoName,
                        it,
                        restartTransition
                    )
                }
            }

            binding.root.setOnLongClickListener {
                Log.i("EditTest","longClicked")
                editFun(item.entity,binding.ivThumbnail)
                true
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

        fun bindImage(
            view: ImageView,
            imageName: String? = null,
            ingType: IngredientCategoryType,
            restartTransition: Boolean
        ) {
            Logger.i()
            val storage = RefereeApplication.instance.applicationContext.cacheDir
            val source = imageName?.let {
                File(storage, imageName)
            }
            val padding =
                view.context.resources.getDimension(R.dimen.ingredient_placeholder_inset)
                    .toInt()
            val drawable =
                ResourcesCompat.getDrawable(
                    view.context.resources,
                    ingType.iconResourceId,
                    null
                )
            val insetDrawable =
                InsetDrawable(drawable, CommonUtil.pxToDp(view.context, padding))

            Glide
                .with(view.context)
                .load(source)
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (restartTransition) {
                            if(adapterPosition ==updatedPosition) {
                                view.post {
                                    ((view.context) as Activity).startPostponedEnterTransition()
                                    view.transitionName = null
                                    updatedPosition = null
                                }
                            }
                        }

                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (restartTransition) {
                            if(adapterPosition ==updatedPosition) {
                                view.post {
                                    ((view.context) as Activity).startPostponedEnterTransition()
                                    view.transitionName = null
                                    updatedPosition = null
                                }
                            }
                        }
                        return false
                    }
                })
                .placeholder(insetDrawable)
                .into(view)
        }
    }
}