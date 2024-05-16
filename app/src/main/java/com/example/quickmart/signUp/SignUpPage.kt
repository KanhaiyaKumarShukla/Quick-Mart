package com.example.quickmart.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quickmart.R
import com.example.quickmart.databinding.ActivitySignUpPageBinding
import com.example.quickmart.login.LoginPage
import com.example.quickmart.utils.AppConstant
import com.google.firebase.auth.FirebaseAuth

class SignUpPage : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.loginNavigation.setOnClickListener {
            val intent= Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpUser(){
        val email:String=binding.emailAddressEt.text.toString()
        val password=binding.passwordEt.text.toString()
        val confirmPassword=binding.cnfPasswordEt.text.toString()

        val verifyEmail= AppConstant.verifyEmail(email)
        val verifyPassword= AppConstant.verifyPassword(password)
        val verifyConfirmPassword=AppConstant.verifyConfirmPassword(password, confirmPassword)

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
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error creating user.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}