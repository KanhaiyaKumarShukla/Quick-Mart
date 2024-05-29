package com.example.quickmart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private var _binding:FragmentAccountBinding ? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            // redirect to the main fragment
            Log.d("account fragment", currentUser.email.toString())
            findNavController().navigate(R.id.action_accountFragment_to_userProfileFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        Glide.with(this)
            .load("https://th.bing.com/th/id/OIP.p0b5pSAekHfyI5A3-2mYEQAAAA?rs=1&pid=ImgDetMain")
            .placeholder(R.drawable.baseline_wb_sunny_24)
            .override(70, 70)
            .into(binding.discountImage)

        Glide.with(this)
            .load("https://png.pngtree.com/png-clipart/20230211/original/pngtree-free-delivery-truck-icon-png-image_8951758.png")
            .placeholder(R.drawable.baseline_wb_sunny_24)
            .override(70, 70)
            .into(binding.freeDeliveryImage)

        Glide.with(this)
            .load("https://cdn4.iconfinder.com/data/icons/logistics-retro-pack-vol-1/100/easy_return-512.png")
            .placeholder(R.drawable.baseline_wb_sunny_24)
            .override(70, 70)
            .into(binding.EasyReturnImage)

        Glide.with(this)
            .load("https://cdn4.iconfinder.com/data/icons/e-commerce-and-business/106/Pay_on_Delivery-512.png")
            .placeholder(R.drawable.baseline_wb_sunny_24)
            .override(70, 70)
            .into(binding.payOnDeliveryImage)

        binding.logInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_logInFragment)
        }
        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_signUpFragment)
        }

    }
}