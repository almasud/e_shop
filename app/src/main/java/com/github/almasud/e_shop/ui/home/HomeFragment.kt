package com.github.almasud.e_shop.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textviewHome.text = getString(R.string.home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}