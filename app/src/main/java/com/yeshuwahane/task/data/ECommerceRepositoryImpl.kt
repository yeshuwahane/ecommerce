package com.yeshuwahane.task.data

import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.domain.ECommerceRepository
import javax.inject.Inject

class ECommerceRepositoryImpl @Inject constructor(val api: EcommerceApi) : ECommerceRepository {

    override suspend fun getProductDetails(): WSModel {
        return api.getProductDetails()
    }
}