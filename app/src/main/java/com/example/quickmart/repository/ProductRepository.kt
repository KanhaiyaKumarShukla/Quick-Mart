package com.example.quickmart.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.retrofit.APIService
import com.example.quickmart.utils.AppConstant
import com.example.quickmart.utils.NetworkResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api:APIService) {
    private val _products=MutableLiveData<NetworkResult<List<productsItem>>>()
    val products: LiveData<NetworkResult<List<productsItem>>>
        get() = _products
    suspend fun getProducts(){
        _products.value=NetworkResult.Loading()
        val result=api.getProducts()
        result.enqueue(object : Callback<List<productsItem>>{
            override fun onResponse(
                call: Call<List<productsItem>>,
                response: Response<List<productsItem>>
            ) {
                if(response.isSuccessful)Log.d("falure", "response falure")
                if(response.body()==null)Log.d("empty", "empty body")
                if(response.isSuccessful && response.body()!=null){
                    val productList=response.body()
                    _products.value=NetworkResult.Success(productList!!)
                    Log.d("successful result", productList.toString())
                }else if(response.errorBody()==null){
                    _products.value=NetworkResult.Error("Something went Wrong")
                    Log.d("emptyBody","something error")
                }else{
                    Log.d("donotKnow","something error")
                    _products.value=NetworkResult.Error("Something went Wrong")
                }
            }

            override fun onFailure(call: Call<List<productsItem>>, t: Throwable) {
                Log.e(AppConstant.TAG,"Error Found! api response failed")
                _products.value=NetworkResult.Error("Something went Wrong")
            }
        })
    }

    private val _productsByCategory=MutableLiveData<NetworkResult<List<productsItem>>>()
    val productsByCategory: LiveData<NetworkResult<List<productsItem>>>
        get() = _productsByCategory

    fun getProductByCategory(category:String){
        val result=api.getProductsByCategory(category)
        result.enqueue(object:Callback<List<productsItem>>{
            override fun onResponse(
                call: Call<List<productsItem>>,
                response: Response<List<productsItem>>
            ) {
                if(response.isSuccessful && response.body()!=null){
                    val productList=response.body()
                    _productsByCategory.value=NetworkResult.Success(productList!!)
                    Log.d("successful result", productList.toString())
                }else if(response.errorBody()==null){
                    _productsByCategory.value=NetworkResult.Error("Something went Wrong")
                    Log.d("emptyBody","something error")
                }else{
                    Log.d("donotKnow","something error")
                    _productsByCategory.value=NetworkResult.Error("Something went Wrong")
                }
            }

            override fun onFailure(call: Call<List<productsItem>>, t: Throwable) {
                _productsByCategory.value=NetworkResult.Error("Something went Wrong")
            }
        })
    }

    private val _productsByCategoryAtLimit=MutableLiveData<NetworkResult<List<productsItem>>>()
        val productsByCategoryAtLimit: LiveData<NetworkResult<List<productsItem>>>
        get() = _productsByCategoryAtLimit

    fun getProductByResponseAtLimit(category: String, limit:Int){
        val result=api.getProductsByCategoryAtLimit(category, limit)
        result.enqueue(object:Callback<List<productsItem>>{
            override fun onResponse(
                call: Call<List<productsItem>>,
                response: Response<List<productsItem>>
            ) {
                if(response.isSuccessful && response.body()!=null){
                    val productList=response.body()
                    _productsByCategoryAtLimit.value=NetworkResult.Success(productList!!)
                    Log.d("successful result", productList.toString())
                }else if(response.errorBody()==null){
                    _productsByCategoryAtLimit.value=NetworkResult.Error("Something went Wrong")
                    Log.d("emptyBody","something error")
                }else{
                    Log.d("donotKnow","something error")
                    _productsByCategoryAtLimit.value=NetworkResult.Error("Something went Wrong")
                }
            }

            override fun onFailure(call: Call<List<productsItem>>, t: Throwable) {
                _productsByCategoryAtLimit.value=NetworkResult.Error("Something went Wrong")
            }
        })
    }

}