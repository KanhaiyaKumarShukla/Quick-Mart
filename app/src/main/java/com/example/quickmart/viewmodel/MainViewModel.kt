package com.example.quickmart.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.repository.ProductRepository
import com.example.quickmart.utils.NetworkResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


//// https://www.geeksforgeeks.org/assisted-dependency-injection-in-viewmodel-with-dagger-hilt-in-android/

class MainViewModel @AssistedInject constructor(
    private val repository: ProductRepository,
    @Assisted private val category: String?,
    @Assisted private val limit: Int?
) : ViewModel() {

    val productsLiveData: LiveData<NetworkResult<List<productsItem>>>
        get() = repository.products

    val productsByCategory: LiveData<NetworkResult<List<productsItem>>>
        get() = repository.productsByCategory

    val productsByCategoryAtLimit: LiveData<NetworkResult<List<productsItem>>>
        get() = repository.productsByCategoryAtLimit

    val productsByCategoriesMap:LiveData<NetworkResult<Map<String, List<productsItem>>>>
        get() = repository.productsByCategoryMap

    val categories:LiveData<NetworkResult<List<String>>>
        get() = repository.categories

    init {
        viewModelScope.launch {
            repository.getProducts()
            repository.getCategories()
            if (category != null && limit != null) repository.getProductByCategoryAtLimit(category, limit)
            if (category != null) repository.getProductByCategory(category)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(category: String?, limit: Int?): MainViewModel
    }

    companion object {
        fun providesFactory(
            assistedFactory: Factory,
            category: String?,
            limit: Int?
        ): ViewModelProvider.Factory{
            return object : ViewModelProvider.Factory {
                //@Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(category, limit) as T
                }
            }
        }
    }
}