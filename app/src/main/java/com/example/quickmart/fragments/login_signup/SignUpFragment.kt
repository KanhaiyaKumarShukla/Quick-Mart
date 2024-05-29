package com.example.quickmart.fragments.login_signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quickmart.R
import com.example.quickmart.databinding.FragmentSignUpBinding
import com.example.quickmart.utils.AppConstant
import com.google.firebase.auth.FirebaseAuth


// for backstack https://www.youtube.com/watch?v=V_LCfFVjs4c&list=PLRKyZvuMYSIMO2ebTldbwMTnDCn5klzjS&index=4
class SignUpFragment : Fragment() {

    private  var _binding:FragmentSignUpBinding?=null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            if(firebaseAuth.currentUser!=null){
                 if(firebaseAuth.currentUser!!.isEmailVerified) findNavController().navigate(R.id.action_signUpFragment_to_userProfileFragment)
                 else Toast.makeText(requireContext(), "You already have an account, please login in", Toast.LENGTH_SHORT).show()
            }else {
                signUpUser()
            }
        }
        binding.loginNavigation.setOnClickListener {
             findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            // redirect to the main fragment
            findNavController().navigate(R.id.action_signUpFragment_to_userProfileFragment)
        }
    }

    private fun signUpUser(){
        val email:String=binding.emailAddressEt.text.toString()
        val password=binding.passwordEt.text.toString()
        val confirmPassword=binding.cnfPasswordEt.text.toString()

        val verifyEmail= AppConstant.verifyEmail(email)
        val verifyPassword= AppConstant.verifyPassword(password)
        val verifyConfirmPassword= AppConstant.verifyConfirmPassword(password, confirmPassword)

        if(!verifyPassword.first && !verifyEmail.first && !verifyConfirmPassword.first){
            binding.emailTextInputLayout.error=verifyEmail.second
            binding.passwordTextInputLayout.error=verifyPassword.second
            binding.confirmPasswordTextInputLayout.error=verifyConfirmPassword.second
            return
        }else if(!verifyPassword.first && !verifyEmail.first){
            binding.emailTextInputLayout.error=verifyEmail.second
            binding.passwordTextInputLayout.error=verifyPassword.second
            return
        }else if(!verifyPassword.first && !verifyConfirmPassword.first){
            binding.passwordTextInputLayout.error=verifyPassword.second
            binding.confirmPasswordTextInputLayout.error=verifyConfirmPassword.second
            return
        }else if(!verifyConfirmPassword.first && !verifyEmail.first){
            binding.emailTextInputLayout.error=verifyEmail.second
            binding.confirmPasswordTextInputLayout.error=verifyConfirmPassword.second
            return
        }else if(!verifyConfirmPassword.first){
            binding.confirmPasswordTextInputLayout.error=verifyConfirmPassword.second
            return
        }else if(!verifyEmail.first) {
            binding.emailTextInputLayout.error = verifyEmail.second
            return
        }else if(!verifyPassword.first){
            binding.passwordTextInputLayout.error=verifyPassword.second
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(requireContext(), "Sign Up Successful: ${it.result.user?.email}", Toast.LENGTH_SHORT).show()

                    firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(requireContext(), "User registered successfully. Please verify your email id", Toast.LENGTH_LONG).show()
                            if(firebaseAuth.currentUser!!.isEmailVerified)findNavController().navigate(R.id.action_signUpFragment_to_userProfileFragment)
                        }else{
                            Toast.makeText(requireContext(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(requireContext(), "Error creating user: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }


    }
}