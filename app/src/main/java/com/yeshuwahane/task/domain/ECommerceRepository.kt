package com.yeshuwahane.task.domain

import com.yeshuwahane.task.data.model.WSModel


interface ECommerceRepository {
    suspend fun getProductDetails(): WSModel

}