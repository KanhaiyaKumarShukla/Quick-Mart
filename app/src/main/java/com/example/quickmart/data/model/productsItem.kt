package com.example.quickmart.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class productsItem(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
): Parcelable