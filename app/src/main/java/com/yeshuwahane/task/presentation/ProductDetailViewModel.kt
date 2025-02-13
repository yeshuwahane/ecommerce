package com.yeshuwahane.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeshuwahane.task.data.ECommerceRepositoryImpl
import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.utils.DataResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(val repository: ECommerceRepositoryImpl): ViewModel() {

    private val _productDetailsState = MutableStateFlow(ProductDetailState(productDetails = DataResource.loading()))
    val productDetailsState = _productDetailsState
        .onStart {
            getProductDetails()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductDetailState(productDetails = DataResource.loading())
        )


    private fun getProductDetails(){
        viewModelScope.launch {
            val productDetials = repository.getProductDetails()
            _productDetailsState.update {
                it.copy(productDetials)
            }
        }
    }

}




data class ProductDetailState(val productDetails: DataResource<WSModel?> = DataResource.initial())



