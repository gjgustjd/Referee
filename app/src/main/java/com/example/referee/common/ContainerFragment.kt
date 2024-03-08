package com.example.referee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.referee.R
import com.example.referee.databinding.FragmentContainerBinding

class ContainerFragment : Fragment() {
    private lateinit var binding:FragmentContainerBinding
    var childFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_container,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragment?.let {
            setFragment(it)
        }
    }

    fun setFragment(fragment: Fragment) {
        childFragment = fragment
        childFragmentManager
            .beginTransaction()
            .add(R.id.fcnContainer, fragment)
            .commit()
    }

}