package com.example.referee.ingredients

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.databinding.FragmentIngredientsBinding
import com.example.referee.ingredientadd.IngredientAddActivity
import com.example.referee.ingredientadd.model.IngredientEntity

class IngredientsFragment : Fragment() {

    lateinit var binding:FragmentIngredientsBinding
    private val viewModel by activityViewModels<IngredientsFragmentViewModel>()
    private var ingredientAdapter: IngredientsAdapter? = null
    private val decoration by lazy {
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 30
                outRect.left = 30
                outRect.bottom = 30
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_ingredients, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getIngredientsList()
    }

    private fun initViews()  {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding.rvIngredients) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(decoration)
        }
    }

    private fun updateRecyclerView(items: List<IngredientEntity>) {
        ingredientAdapter = IngredientsAdapter(items)
        binding.rvIngredients.adapter = ingredientAdapter
    }

    private fun initListener() {
        binding.fabAddIngredient.setOnClickListener {
            startActivity(Intent(requireActivity(), IngredientAddActivity::class.java))
        }
        viewModel.event.observe(requireActivity()) {
            when (it.getContentIfNotHandled()) {
                is IngredientsEvent.GetIngredients.Success -> {
                    val event = it.peekContent() as IngredientsEvent.GetIngredients.Success
                    updateRecyclerView(event.ingredients)
                }

                else -> Unit
            }
        }
    }
}