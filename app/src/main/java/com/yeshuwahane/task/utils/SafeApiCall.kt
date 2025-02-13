package com.yeshuwahane.task.utils

import retrofit2.Response


suspend inline fun <reified T : Any> safeApiCall(apiCall: () -> Response<T>): DataResource<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                DataResource.success(it)
            } ?: DataResource.error(Throwable("No data received"), null)
        } else if (response.code() == 401) {
            DataResource.error(Throwable("401 Unauthorized"), null)
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown Error"
            DataResource.error(Throwable(errorBody), null)
        }
    } catch (exception: Exception) {
        DataResource.error(exception, null)
    }
}