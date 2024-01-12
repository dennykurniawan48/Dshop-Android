package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.checkisinfavorite.CheckIsInFavorite
import com.dennydev.dshop.model.detailproduct.DetailProduct
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.review.ReviewResponse
import com.dennydev.dshop.network.ApiDetailProductImpl
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.CartOrderRepository
import com.dennydev.dshop.repository.DetailProductRepository
import com.dennydev.dshop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: DetailProductRepository,
    private val cartRepository: CartOrderRepository
): ViewModel() {
    val token = datastoreRepository.flowToken
    private val _detailProduct = mutableStateOf<ApiResponse<DetailProduct>>(ApiResponse.Idle())
    val detailProduct: State<ApiResponse<DetailProduct>> = _detailProduct

    private val _productInFav = mutableStateOf(false)
    val productInFav: State<Boolean> = _productInFav

    private val _isInFavorite = mutableStateOf<ApiResponse<CheckIsInFavorite>>(ApiResponse.Idle())
    val isInFavorite: State<ApiResponse<CheckIsInFavorite>> = _isInFavorite

    private val _review = mutableStateOf<ApiResponse<ReviewResponse>>(ApiResponse.Idle())
    val review: State<ApiResponse<ReviewResponse>> = _review

    private val _itemExist = mutableStateOf(false)
    val itemExist: State<Boolean> = _itemExist

    fun checkItemInDatabase(id: String){
        viewModelScope.launch {
            cartRepository.checkItemFromCart(id).collectLatest {
                _itemExist.value = it.isNotEmpty()
            }
        }
    }

    fun getDetailProduct(token: String, id: String){
        _detailProduct.value = ApiResponse.Loading()
        _isInFavorite.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _detailProduct.value = repository.getDetailProduct(id)
            _isInFavorite.value = repository.getIsProductInFavorite(token, id)
            _review.value = repository.getReviewProduct(id)
        }
    }

    fun addItemToCart(id: String){
        viewModelScope.launch {
            cartRepository.addToCart(OrderCart(productId = id, qty = 1))
            _itemExist.value = true
        }
    }

    fun onChangeProductFav(new: Boolean){
        _productInFav.value = new
    }

    fun removeFromFav(id: String, token: String){
        _productInFav.value = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavorite(id,token)
        }
    }

    fun addToFavorite(id: String,token: String){
        _productInFav.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorite(id,token)
        }
    }
}