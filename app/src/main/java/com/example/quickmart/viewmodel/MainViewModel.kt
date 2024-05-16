package com.example.quickmart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.repository.ProductRepository
import com.example.quickmart.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {
    val productsLiveData: LiveData<NetworkResult<List<productsItem>>>
        get() = repository.products
    init {
        viewModelScope.launch {
            repository.getProducts()
        }
    }
}