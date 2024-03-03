package com.example.referee.ingredients

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.EventWrapper
import com.example.referee.common.ItemTouchHelperListener
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.FragmentIngredientsBinding
import com.example.referee.ingredientadd.IngredientAddActivity
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredients.model.IngredientFragFABState
import com.example.referee.ingredients.model.IngredientItemTouchHelperCallback
import com.example.referee.ingredients.model.IngredientsSelectableItem
import kotlinx.coroutines.launch

class IngredientsFragment :
    BaseFragment<FragmentIngredientsBinding>(R.layout.fragment_ingredients),ItemTouchHelperListener {

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
    lateinit var itemTouchHelper: ItemTouchHelper
    private val subFabArray by lazy { arrayOf(binding.fabDelete, binding.fabSearch) }
    private val animDuration by lazy {
        resources.getInteger(R.integer.animation_default_duration).toLong()
    }

    override fun initView() {
        initRecyclerView()
        binding.viewModel = viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun initListeners() {
        lifecycleScope.launch {
            Log.i("DeleteTest", "sharedFlow observe")
            viewModel.sharedFlow.collect {
                Log.i("DeleteTest", "sharedFlow collect")
                when (it.getContentIfNotHandled()) {
                    is IngredientsEvent.GetIngredients.Success -> {
                        val event = it.peekContent() as IngredientsEvent.GetIngredients.Success
                        updateRecyclerView(event.ingredients)
                        hideLoading()
                    }

                    is IngredientsEvent.DeleteIngredients.Success -> {
                        Log.i("DeleteTest", "DeleteSuccess")

                        hideLoading()
                        showToast(getString(R.string.ingredient_delete_success_toast))
                        val state = viewModel.fabState.value?.peekContent()

                        if (state == IngredientFragFABState.DeleteMenu) {
                            onMainFabClick()
                        }
                    }

                    is IngredientsEvent.DeleteIngredients.Failed -> {
                        hideLoading()
                        showToast(getString(R.string.ingredient_delete_failed_toast))
                    }

                    is IngredientsEvent.IngredientBitmap -> {
                        val event = it.peekContent() as IngredientsEvent.IngredientBitmap

                        hideLoading()
                        ingredientAdapter?.bindThumbnail(event.bitmap, event.position)
                    }

                    else -> Unit
                }
            }
        }

        viewModel.fabState.observe(requireActivity()) {
            when (viewModel.fabState.value?.getContentIfNotHandled()) {
                IngredientFragFABState.None -> {
                    requireActivity().title = getString(R.string.navigation_menu_ingredient)
                }

                IngredientFragFABState.SubMenu -> {
                    requireActivity().title = getString(R.string.navigation_menu_ingredient)
                }

                IngredientFragFABState.DeleteMenu -> {
                    requireActivity().title = getString(R.string.ingredient_delete_title)
                }

                IngredientFragFABState.SearchMenu -> {
                    requireActivity().title = getString(R.string.ingredient_search_title)
                }

                else -> Unit
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean = false
    override fun onItemSwipe(position: Int) {
        ingredientAdapter?.getItems()?.get(position)?.let {item->
            viewModel.removeIngredient(item)
        }
    }
    fun onMainFabClick() {
        Log.i("FabTest","onMainFabClick")
        Log.i("FabTest","value:${viewModel.fabState.value}")
        when (viewModel.fabState.value?.peekContent()) {
            IngredientFragFABState.None -> {
                Log.i("FabTest","None")
                startActivity(Intent(requireActivity(), IngredientAddActivity::class.java))
            }

            IngredientFragFABState.SubMenu -> {
                Log.i("FabTest","SubMenu")
                onMainFabReClick()
            }

            is IngredientFragFABState.DeleteMenu -> {
                Log.i("FabTest","DeleteMenu")
                viewModel.fabState.value = EventWrapper(IngredientFragFABState.None)
                onMainFabLongClick()
                ingredientAdapter?.apply {
                    isDeleteMode = false
                    notifyDataSetChanged()
                }
            }

            else -> Unit
        }
    }

    fun onMainFabLongClick():Boolean {
        Log.i("FabTest","onMainFabLongClick")
        when (viewModel.fabState.value?.peekContent()) {
            IngredientFragFABState.None -> {
                Log.i("FabTest","None")
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

                viewModel.fabState.value = EventWrapper(IngredientFragFABState.SubMenu)
            }

            IngredientFragFABState.SubMenu -> {
                Log.i("FabTest","SubMenu")
                onMainFabReClick()
            }

            else -> Unit
        }

        return true
    }

    private fun onMainFabReClick() {
        Log.i("FabTest","onMainFabReClick")
        val scaleDownAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
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
        viewModel.fabState.value = EventWrapper(IngredientFragFABState.None)
    }

    fun onDeleteFabClick() {
        Log.i("FabTest","onDeleteFabClick")
        val scaleUpAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        val scaleDownAnim =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        when (viewModel.fabState.value?.peekContent()) {
            IngredientFragFABState.SubMenu -> {
                Log.i("FabTest","SubMenu")
                binding.fabAddIngredient.rotation = 0f
                activity?.title = getString(R.string.ingredient_delete_title)
                viewModel.fabState.value = EventWrapper(IngredientFragFABState.DeleteMenu)
                binding.fabAddIngredient.startAnimation(scaleUpAnim)
                binding.fabSearch.let {
                    it.startAnimation(scaleDownAnim)
                    it.hide()
                }
                ingredientAdapter?.apply {
                    isDeleteMode = true
                    notifyDataSetChanged()
                }
            }

            IngredientFragFABState.DeleteMenu -> {
                Log.i("FabTest","DeleteMenu")
                ingredientAdapter?.getSelectedItem()?.let { list ->
                    list.ifEmpty {
                        showToast(getString(R.string.ingredient_delete_empty_toast))
                       return@let
                    }

                    showLoading()
                    viewModel.removeIngredients(list)
                }
            }

            else -> Unit
        }
    }

    private fun initRecyclerView() {
        itemTouchHelper = ItemTouchHelper(IngredientItemTouchHelperCallback(this))
        itemTouchHelper.attachToRecyclerView(binding.rvIngredients)
        with(binding.rvIngredients) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
        showLoading()
        viewModel.getIngredientsList()
    }

    private fun updateRecyclerView(items: List<IngredientEntity>) {
        ingredientAdapter = IngredientsAdapter(
            items.map { IngredientsSelectableItem(it) }.toMutableList(),
            ::editItem
        ) { imageName, position ->
            showLoading()
            viewModel.getImageBitmap(imageName, position)
        }.apply {
            setHasStableIds(true)
        }

        binding.rvIngredients.adapter = ingredientAdapter
    }

    private fun editItem(item: IngredientEntity, sharedView: View) {
        Log.i("EditTest", "editItem:$item")
        startActivity(
            IngredientAddActivity.newIntent(requireContext(), true, item),
            ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                sharedView,
                sharedView.transitionName
            ).toBundle()
        )

    }
}