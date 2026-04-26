package com.svb.fieldops.core.common

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val message: String,
        val cause: Throwable? = null,
    ) : NetworkResult<Nothing>()
}
