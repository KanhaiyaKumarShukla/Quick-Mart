package com.example.quickmart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentIntroBinding
import com.example.quickmart.databinding.FragmentSignUpBinding

class IntroFragment : Fragment() {


    private  var _binding: FragmentIntroBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentIntroBinding.inflate(inflater, container, false)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                findNavController().navigate(R.id.action_introFragment_to_homeFragment)
            }, 3000)
        return binding.root
    }

}