package com.example.referee.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.referee.R
import com.example.referee.databinding.FragmentIngredientsBinding
import com.example.referee.ingredientadd.model.IngredientEntity

class IngredientsFragment : Fragment() {

    lateinit var binding:FragmentIngredientsBinding
    private val viewModel by activityViewModels<IngredientsFragmentViewModel>()
    private var ingredientAdapter: IngredientsAdapter? = null

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

    }

    private fun initRecyclerView(items:List<IngredientEntity>) {
        ingredientAdapter = IngredientsAdapter(items)
        with(binding.rvIngredients) {
            adapter = ingredientAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initListener() {
        binding.fabAddIngredient.setOnClickListener {
            findNavController().navigate(R.id.action_ingredientsFragment_to_ingredientAddActivity)
        }
        viewModel.event.observe(requireActivity()) {
            when (it.getContentIfNotHandled()) {
                is IngredientsEvent.GetIngredients.Success -> {
                    val event = it.peekContent() as IngredientsEvent.GetIngredients.Success
                    initRecyclerView(event.ingredients)
                }

                else -> Unit
            }
        }
    }
}