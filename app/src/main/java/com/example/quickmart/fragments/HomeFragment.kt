package com.example.quickmart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentHomeBinding
import com.example.quickmart.utils.NetworkResult
import com.example.quickmart.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel= ViewModelProvider(this)[MainViewModel::class.java]
        mViewModel.productsLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success->Log.d("data retrieved:", it.data.toString())
                is NetworkResult.Error->Log.d("data Retrieved", "error found!")
                is NetworkResult.Loading->{
                     Log.d("data Retrieved:", "Loading...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}