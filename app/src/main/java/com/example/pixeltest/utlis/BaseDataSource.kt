package com.example.pixeltest.utlis

import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                return if (body != null  ) {
                    Resource.success(body)
                } else {
                    Resource.error("Body is null")
                }
            }
            val errorBody = response.errorBody().toString()
            return error("${response.code()} - $errorBody")
        } catch (err: Exception) {
            return error("${err.localizedMessage} - ${err.message}")
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network error: $message")
    }
}