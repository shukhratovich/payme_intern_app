package com.example.domain.model


sealed class Network<out T> {
    data class Success<T>(val data: T): Network<T>()
    data class Error(val message: Throwable): Network<Nothing>()
}

data class Error(
    val status: String?,
    val code: String?
)