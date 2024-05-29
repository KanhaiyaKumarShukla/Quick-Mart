package com.example.quickmart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentErrorBinding
import com.example.quickmart.databinding.FragmentHomeBinding
import com.example.quickmart.utils.AppConstant


class ErrorFragment : Fragment() {

    private var _binding :FragmentErrorBinding?=null
    private val binding get() = _binding!!

    private var destination: Int? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            destination=it.getInt("destination")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(AppConstant.isInternetAvailable(requireContext())){
            destination?.let {
                findNavController().navigate(destination!!)
            }
        }
    }
}