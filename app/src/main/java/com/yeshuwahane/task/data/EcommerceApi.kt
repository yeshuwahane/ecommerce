package com.yeshuwahane.task.data

import com.yeshuwahane.task.data.model.WSModel
import retrofit2.http.GET

interface EcommerceApi {

    @GET("productdetails/6701/253620?lang=en&store=KWD")
    suspend fun getProductDetails(): WSModel

}