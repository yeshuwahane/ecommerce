package com.yeshuwahane.task.data

import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.utils.DataResource
import retrofit2.Response
import retrofit2.http.GET

interface EcommerceApi {

    @GET("productdetails/6701/253620?lang=en&store=KWD")
    suspend fun getProductDetails(): Response<WSModel>

}