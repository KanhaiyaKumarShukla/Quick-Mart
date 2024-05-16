package com.example.quickmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quickmart.databinding.ActivityIntroPageBinding
import com.example.quickmart.login.LoginPage
import com.example.quickmart.signUp.SignUpPage
import com.google.firebase.auth.FirebaseAuth

class IntroPage : AppCompatActivity() {
    private lateinit var binding: ActivityIntroPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIntroPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth=FirebaseAuth.getInstance()
        val redirectTo= if(auth.currentUser!=null){
            "MAIN"
        }else "LOGIN"
        binding.startBtn.setOnClickListener {
            try {
                redirect(redirectTo)
            }catch (e:Exception){
                Toast.makeText(this, "No path Found!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun redirect(s: String) {
        val intent=when(s){
            "MAIN"->Intent(this, MainActivity::class.java)
            "LOGIN"->Intent(this, LoginPage::class.java)
            else->throw Exception("no path Exist")
        }
        startActivity(intent)
    }
}