package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.detailorder.DetailOrder
import com.dennydev.dshop.model.detailproduct.DetailProduct
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.DetailOrderRepository
import com.dennydev.dshop.repository.DetailProductRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailOrderViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: DetailOrderRepository
): ViewModel() {
    private val _detailProduct = mutableStateOf<ApiResponse<DetailOrder>>(ApiResponse.Idle())
    val detailProduct: State<ApiResponse<DetailOrder>> = _detailProduct

    private val _checkoutState = MutableStateFlow<PaymentSheetResult?>(null)
    val checkoutState: StateFlow<PaymentSheetResult?> = _checkoutState

    private val _paymentSheetdata = mutableStateOf<ApiResponse<PaymentSheetData>>(ApiResponse.Idle())
    val paymentSheetdata: State<ApiResponse<PaymentSheetData>> = _paymentSheetdata

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        _checkoutState.update { paymentSheetResult }
    }

    fun getPaymentSheetData(total: Double, token: String, orderId: String){
        _paymentSheetdata.value = ApiResponse.Loading()
        viewModelScope.launch {
            _paymentSheetdata.value = repository.getPaymentSheetData(total, token, orderId)
        }
    }

    fun getDetailProduct(id: String){
        _detailProduct.value = ApiResponse.Idle()
        viewModelScope.launch(Dispatchers.IO) {
            _detailProduct.value = repository.getDetailOrder(id)
        }
    }

    fun resetPaymentSheet(){
        _paymentSheetdata.value = ApiResponse.Idle()
        _checkoutState.value = null
    }
}