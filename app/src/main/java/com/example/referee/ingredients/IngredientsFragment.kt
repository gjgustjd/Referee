package com.example.referee.ingredients

import android.content.Intent
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentIngredientsBinding
import com.example.referee.ingredientadd.IngredientAddActivity
import com.example.referee.ingredientadd.model.IngredientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredientsFragment :
    BaseFragment<FragmentIngredientsBinding>(R.layout.fragment_ingredients) {

    private val viewModel by activityViewModels<IngredientsFragmentViewModel>()
    private var ingredientAdapter: IngredientsAdapter? = null
    private val decoration by lazy {
        val margin = CommonUtil.pxToDp(
            requireContext(),
            resources.getDimension(R.dimen.decorator_default_margin).toInt()
        )

        CommonRecyclerViewDecoration(
            rightMargin = margin,
            leftMargin = margin,
            bottomMargin = margin
        )
    }
    private val subFabArray by lazy { arrayOf(binding.fabDelete, binding.fabSearch) }
    private val animDuration by lazy {
        resources.getInteger(R.integer.animation_default_duration).toLong() ?: 300L
    }

    override fun initView() {
        initRecyclerView()
    }

    override fun initListeners() {
        with(binding.fabAddIngredient) {
            setOnClickListener {
                if (rotation == 0f) {
                    startActivity(Intent(requireActivity(), IngredientAddActivity::class.java))
                } else {
                    onMainFabReClick()
                }
            }

            setOnLongClickListener {
                if (rotation == 0f) {
                    onMainFabLongClick()
                } else {
                    onMainFabReClick()
                }

                true
            }
        }
        viewModel.event.observe(requireActivity()) {
            when (it.getContentIfNotHandled()) {
                is IngredientsEvent.GetIngredients.Success -> {
                    val event = it.peekContent() as IngredientsEvent.GetIngredients.Success
                    updateRecyclerView(event.ingredients)
                    hideLoading()
                }

                else -> Unit
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.bitmapFlow.collect { event ->
                event?.let {
                    hideLoading()
                    ingredientAdapter?.bindThumbnail(event.bitmap, event.position)
                }
            }
        }
    }

    private fun onMainFabLongClick() {
        val scaleUpAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        with(binding.fabAddIngredient) {
            animate().apply {
                rotation(45f)
                duration = animDuration
                interpolator = DecelerateInterpolator()
                start()
            }

            subFabArray.forEach { fab ->
                fab.show()
                fab.startAnimation(scaleUpAnim)
            }
        }
    }

    private fun onMainFabReClick() {
        val scaleDownAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        with(binding.fabAddIngredient) {
            animate().apply {
                rotation(0f)
                duration = animDuration
                interpolator = DecelerateInterpolator()
                start()
            }

            subFabArray.forEach { fab ->
                fab.startAnimation(scaleDownAnim)
                fab.hide()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.rvIngredients) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
        showLoading()
        viewModel.getIngredientsList()
    }

    private fun updateRecyclerView(items: List<IngredientEntity>) {
        ingredientAdapter = IngredientsAdapter(
            items
        ) { imageName, position ->
            showLoading()
            viewModel.getImageBitmap(imageName, position)
        }
        binding.rvIngredients.adapter = ingredientAdapter
    }
}