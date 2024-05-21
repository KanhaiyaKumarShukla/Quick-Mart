package com.example.quickmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainViewModelProviderFactory(
    private val assistedFactory: MainViewModel.MainViewModelFactory,
    private val category: String?,
    private val limit: Int?
    ): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return assistedFactory.create(category, limit) as T
        }
    }
