package com.example.quickmart.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.quickmart.R
import com.example.quickmart.databinding.ActivityLoginPageBinding
import com.example.quickmart.signUp.SignUpPage
import com.example.quickmart.utils.AppConstant
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflates the layout and generates a binding object that grants you direct access to your layout's views.
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        // sets the root view of the inflated layout as the content view of the activity.
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.signUpNavigation.setOnClickListener {
            val intent= Intent(this, SignUpPage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email=binding.emailAddressEt.text.toString()
        val password=binding.passwordEt.text.toString()

        val verifyEmail=AppConstant.verifyEmail(email)
        val verifyPassword=AppConstant.verifyPassword(password)
        if(!verifyEmail.first and !verifyPassword.first){
            binding.emailTextInputLayout.error=verifyEmail.second.toString()
            binding.passwordTextInputLayout.error=verifyPassword.second.toString()
            return
        }else if(!verifyEmail.first){
            binding.emailTextInputLayout.error=verifyEmail.second
            return
        }else if(!verifyPassword.first){
            binding.passwordTextInputLayout.error=verifyPassword.second
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener (this){
                if(it.isSuccessful){
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error creating user.", Toast.LENGTH_SHORT).show()
                }
            }
    }


}