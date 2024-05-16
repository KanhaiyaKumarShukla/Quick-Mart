package com.example.quickmart.utils

import android.content.Context
import android.util.Patterns
import com.example.quickmart.MainActivity

object AppConstant {
    val TAG=MainActivity::class.simpleName
    const val BASE_URL="https://fakestoreapi.com/"
    fun verifyEmail(email:String):Pair<Boolean, String>{
        var result=Pair(true, "")
        if(email.isBlank()){
            result=Pair(false, "Email must be provided.")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result=Pair(false, "Enter valid Email")
        }
        return result
    }
    fun verifyPassword(password:String):Pair<Boolean, String>{
        var result=Pair(true, "")
        if(password.isBlank()){
            result=Pair(false, "Password must be provided.")
        }else if(password.length<6){
            result=Pair(false, "Password must be of 6 character")
        }
        return result
    }
    fun verifyConfirmPassword(password:String, confirmPassword:String):Pair<Boolean, String>{
        var result=Pair(true, "")
        if(password.isBlank() || confirmPassword.isBlank())result=Pair(false, "Password must be provided.")
        else if(password!=confirmPassword)result=Pair(false, "ConfirmPassword must match with Password")
        return result
    }
}