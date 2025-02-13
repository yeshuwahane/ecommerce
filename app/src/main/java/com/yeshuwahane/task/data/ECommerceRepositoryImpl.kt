package com.yeshuwahane.task.data

import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.domain.ECommerceRepository
import com.yeshuwahane.task.utils.DataResource
import com.yeshuwahane.task.utils.safeApiCall
import javax.inject.Inject

class ECommerceRepositoryImpl @Inject constructor(val api: EcommerceApi) : ECommerceRepository {

    override suspend fun getProductDetails(): DataResource<WSModel?> {
        return safeApiCall {
            api.getProductDetails()
        }
    }
}