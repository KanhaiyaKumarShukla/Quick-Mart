package com.example.quickmart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickmart.R
import com.example.quickmart.adapters.CategoryWiseProductAdapter
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.databinding.CategoryWiseProductsBinding
import com.example.quickmart.databinding.FragmentCategoriesBinding
import com.example.quickmart.databinding.FragmentHomeBinding
import com.example.quickmart.utils.NetworkResult
import com.example.quickmart.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: MainViewModel.Factory

    private lateinit var mViewModel: MainViewModel

    private lateinit var categoryWiseData:Map<String, List<productsItem>>

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryWiseProductAdapter:CategoryWiseProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this,
            MainViewModel.providesFactory(mViewModelFactory, null, null)
        )[MainViewModel::class.java]

        mViewModel.productsByCategoriesMap.observe(viewLifecycleOwner) { result ->
            categoryWiseData = when (result) {
                is NetworkResult.Success -> {
                    Log.d("data retrieved:", result.data.toString())
                    result.data!!
                }

                is NetworkResult.Error -> {
                    Log.d("data Retrieved", "error found!")
                    navigateToError()
                    emptyMap()
                }

                is NetworkResult.Loading -> {
                    Log.d("data Retrieved:", "Loading...")
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.categoriesRecycleView.visibility = View.INVISIBLE
                    binding.shimmerLayout.startShimmer()
                    emptyMap()
                }

                else -> {
                    emptyMap()
                }
            }
            if(categoryWiseData.isNotEmpty()){
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.categoriesRecycleView.visibility = View.VISIBLE
                setUpAdapter()
            }

        }



    }

    private fun navigateToError() {
        val bundle:Bundle=Bundle().apply{
            putInt("destination", R.id.action_errorFragment_to_categoriesFragment)
        }
        findNavController().navigate(R.id.action_categoriesFragment_to_errorFragment, bundle)
    }

    private fun setUpAdapter() {
        val categoryWiseProductList:MutableList<Pair<String, List<productsItem>>> = mutableListOf()
        categoryWiseData.forEach {
            val pair=it.toPair()
            categoryWiseProductList.add(pair)
        }
        categoryWiseProductAdapter= CategoryWiseProductAdapter(categoryWiseProductList, findNavController(), R.id.action_categoriesFragment_to_productDetailFragment,2)
        binding.categoriesRecycleView.layoutManager= LinearLayoutManager(requireContext())
        binding.categoriesRecycleView.adapter=categoryWiseProductAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        binding.shimmerLayout.startShimmer()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }
}