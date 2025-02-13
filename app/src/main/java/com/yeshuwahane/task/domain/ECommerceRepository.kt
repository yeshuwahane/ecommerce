package com.yeshuwahane.task.domain

import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.utils.DataResource


interface ECommerceRepository {
    suspend fun getProductDetails(): DataResource<WSModel?>

}