package com.example.referee.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.referee.R
import com.example.referee.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {

    lateinit var binding:FragmentIngredientsBinding
    private val viewModel by viewModels<IngredientsFragmentViewModel>()

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
        initListener()
    }

    private fun initListener() {
        binding.fabAddIngredient.setOnClickListener {
            findNavController().navigate(R.id.action_ingredientsFragment_to_ingredientAddActivity)
        }
    }
}