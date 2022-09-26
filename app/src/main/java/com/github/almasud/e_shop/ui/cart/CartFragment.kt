package com.github.almasud.e_shop.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.github.almasud.e_shop.CategoryListQuery
import com.github.almasud.e_shop.R
import com.github.almasud.e_shop.data.api.ApiClient
import com.github.almasud.e_shop.databinding.FragmentCartBinding


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textviewHome.text = getString(R.string.cart)

        lifecycleScope.launchWhenResumed {
            val response = try {
                ApiClient.apolloClient.query(CategoryListQuery(pagination = 100, skip = 0))
                    .execute()
            } catch (e: ApolloException) {
                Log.e(TAG, "onViewCreated: exception: " + e.message)
                return@launchWhenResumed
            }

            val categories = response.data?.getCategories
            Log.i(TAG, "onViewCreated: categories: $categories")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CartFragment"
    }
}