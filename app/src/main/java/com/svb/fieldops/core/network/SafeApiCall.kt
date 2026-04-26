package com.svb.fieldops.core.network

import com.svb.fieldops.core.common.NetworkResult

suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> =
    try {
        NetworkResult.Success(block())
    } catch (e: Exception) {
        NetworkResult.Error(
            message = e.message ?: "Request failed",
            cause = e,
        )
    }
