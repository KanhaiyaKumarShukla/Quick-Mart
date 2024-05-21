//package com.example.quickmart.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.quickmart.data.model.productsItem
//import com.example.quickmart.repository.ProductRepository
//import com.example.quickmart.utils.NetworkResult
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedFactory
//import dagger.assisted.AssistedInject
//import dagger.hilt.android.lifecycle.HiltViewModel
//import dagger.hilt.android.scopes.ViewModelScoped
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//// https://www.geeksforgeeks.org/assisted-dependency-injection-in-viewmodel-with-dagger-hilt-in-android/
//@HiltViewModel
//class MainViewModel @AssistedInject constructor(private val repository: ProductRepository, @Assisted private val category:String?=null, @Assisted private val limit:Int?=null): ViewModel() {
//
//    val productsLiveData: LiveData<NetworkResult<List<productsItem>>>
//        get() = repository.products
//
//    val productsByCategory: LiveData<NetworkResult<List<productsItem>>>
//        get() = repository.productsByCategory
//
//    val productsByCategoryAtLimit: LiveData<NetworkResult<List<productsItem>>>
//        get() = repository.productsByCategoryAtLimit
//    init {
//        viewModelScope.launch {
//            repository.getProducts()
//            if(category!=null && limit!=null) repository.getProductByResponseAtLimit(category, limit)
//            if(category!=null)repository.getProductByCategory(category)
//        }
//    }
//}


package com.example.quickmart.viewmodel

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

@HiltViewModel
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

    init {
        viewModelScope.launch {
            repository.getProducts()
            if (category != null && limit != null) repository.getProductByResponseAtLimit(category, limit)
            if (category != null) repository.getProductByCategory(category)
        }
    }

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(category: String?, limit: Int?): MainViewModel
    }

    companion object {
        fun providesFactory(
            assistedFactory: MainViewModelFactory,
            category: String?,
            limit: Int?
        ): ViewModelProvider.Factory = MainViewModelProviderFactory(assistedFactory, category, limit)
    }
}
