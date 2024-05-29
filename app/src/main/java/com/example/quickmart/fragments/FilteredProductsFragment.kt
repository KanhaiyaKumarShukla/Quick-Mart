package com.example.quickmart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickmart.R
import com.example.quickmart.adapters.FilteredAdapter
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.databinding.FragmentFilteredProductsBinding

class FilteredProductsFragment : Fragment() {

    private var _binding: FragmentFilteredProductsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFilteredProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productsArray = arguments?.getParcelableArray("extraData")
        val productsList: List<productsItem>? = productsArray?.mapNotNull { it as? productsItem }
        val category=arguments?.getString("extra Data category")
        if(category!=null){
            binding.headingTv.text=category
        }
        Log.d("filtered Products fragment", productsList.toString())
        productsList?.let {
            setUpAdapter(it)
        }

    }

    private fun setUpAdapter(productsList: List<productsItem>) {
        val layoutManager= GridLayoutManager(requireContext(), 2)

        // Set the LayoutManager and adapter for the RecyclerView
        binding.productRv.layoutManager = layoutManager

        val adapter = FilteredAdapter(productsList)
        adapter.setOnClickListener(object:FilteredAdapter.OnClickListener {
            override fun onclick(position: Int, model: productsItem) {
                val bundle = Bundle().apply {
                    putParcelable("extraData", model)
                }
                findNavController().navigate(
                    R.id.action_filteredProductsFragment_to_productDetailFragment,
                    bundle
                )
            }
        })
        binding.productRv.adapter=adapter

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}