package com.example.quickmart.viewmodel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class MainViewModelModule {
    @Provides
    fun provideMainViewModelFactory(
        factory: MainViewModel.MainViewModelFactory
    ): MainViewModel.MainViewModelFactory = factory
}