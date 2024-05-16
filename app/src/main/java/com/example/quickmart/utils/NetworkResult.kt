package com.example.quickmart.utils

//  sealed class is a class that restricts inheritance within a certain defined hierarchy. It is often used to represent restricted class hierarchies in which an object can only be of one of the subclasses of the sealed class.
sealed class NetworkResult<T>(val data:T?=null, val message:String?=null){
    class Success<T>(data:T):NetworkResult<T>(data)
    class Error<T>(message: String?, data: T?=null):NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}
