package com.yeshuwahane.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.domain.ECommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(val repository: ECommerceRepository): ViewModel() {

    private val _productDetailsState: MutableStateFlow<WSModel> = MutableStateFlow(WSModel())
    val productDetailsState = _productDetailsState


    fun getProductDetails(){

        viewModelScope.launch {
            val productDetials = repository.getProductDetails()
            _productDetailsState.value = productDetials
        }

    }

}