package com.dennydev.dshop.model

sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): ApiResponse<T>(data = data, message = null)
    class Error<T>(message: String?, data: T? = null): ApiResponse<T>(data = data, message = message)
    class Loading<T>(): ApiResponse<T>()
    class Idle<T>(): ApiResponse<T>()
}