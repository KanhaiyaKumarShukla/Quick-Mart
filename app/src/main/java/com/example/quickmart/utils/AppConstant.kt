package com.example.quickmart.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.util.Patterns
import com.example.quickmart.MainActivity
import com.example.quickmart.data.model.productsItem
import java.util.Locale

object AppConstant {
    val TAG=MainActivity::class.simpleName
    const val BASE_URL="https://fakestoreapi.com/  "
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
    fun filterProducts(query: String, productsList: List<productsItem>): Map<String, List<productsItem>> {
        val matchingCategory: MutableMap<String, MutableList<productsItem>> = mutableMapOf()
        val matchingTitle: MutableMap<String, MutableList<productsItem>> = mutableMapOf()

        productsList.forEach { product ->
            if (product.category.contains(query, ignoreCase = true)) {
                matchingCategory.getOrPut(product.category) { mutableListOf() }.add(product)
            }
            if (product.title.contains(query, ignoreCase = true)) {
                matchingTitle.getOrPut(product.title) { mutableListOf() }.add(product)
            }
        }

        val mergedMap: MutableMap<String, MutableList<productsItem>> = mutableMapOf()

        // Merge matchingCategory into mergedMap
        matchingCategory.forEach { (key, value) ->
            mergedMap.merge(key, value.toMutableList()) { oldValue, newValue ->
                oldValue.addAll(newValue.filterNot { oldValue.contains(it) })
                oldValue
            }
        }

        // Merge matchingTitle into mergedMap
        matchingTitle.forEach { (key, value) ->
            mergedMap.merge(key, value.toMutableList()) { oldValue, newValue ->
                oldValue.addAll(newValue.filterNot { oldValue.contains(it) })
                oldValue
            }
        }

        return mergedMap
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    fun filterProducts(
        productList: List<productsItem>,
        category: String? = null,
        id: Int? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minRating: Double? = null,
        maxRating: Double? = null
    ): List<productsItem> {
        return productList.filter { product ->
            // Filter by category if provided

            Log.d("filter: ", product.toString())
            if(category!=null){
                Log.d("productList: ", product.category.lowercase(Locale.ROOT))
                Log.d("category passed: ", category.lowercase(Locale.ROOT))
            }else{
                Log.d("category passed: ", "category null")
            }
            if (category != null && product.category.lowercase(Locale.ROOT) != category.lowercase(Locale.ROOT)) {
                return@filter false
            }

            // Filter by id if provided
            if (id != null && product.id != id) {
                return@filter false
            }

            // Filter by price range if provided
            if (minPrice != null && product.price < minPrice) {
                return@filter false
            }
            if (maxPrice != null && product.price > maxPrice) {
                return@filter false
            }

            // Filter by rating range if provided
            if (minRating != null && product.rating.rate < minRating) {
                return@filter false
            }
            if (maxRating != null && product.rating.rate > maxRating) {
                return@filter false
            }

            // Product passed all filters
            true
        }
    }


}