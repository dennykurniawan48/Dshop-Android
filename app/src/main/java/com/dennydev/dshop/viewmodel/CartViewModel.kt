package com.dennydev.dshop.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.ErrorForm
import com.dennydev.dshop.model.cartresponse.CartResponse
import com.dennydev.dshop.model.checkoutresponse.CheckoutResponse
import com.dennydev.dshop.model.countrystate.CountryState
import com.dennydev.dshop.model.countrystate.Data as Country
import com.dennydev.dshop.model.countrystate.State as States
import com.dennydev.dshop.model.delivery.Data as DataDelivery
import com.dennydev.dshop.model.delivery.Delivery
import com.dennydev.dshop.model.form.checkout.CheckoutForm
import com.dennydev.dshop.model.listorder.ListOrder
import com.dennydev.dshop.model.paymentsheetdata.PaymentSheetData
import com.dennydev.dshop.network.ApiCheckCartImpl
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.CartOrderRepository
import com.dennydev.dshop.repository.CheckCartRepository
import com.dennydev.dshop.repository.DetailProductRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CheckCartRepository,
    private val cartRepository: CartOrderRepository
): ViewModel() {
    val cartItems = cartRepository.cartData
    private val _cartProductCount = mutableStateOf(0)
    val cartProductCount: State<Int> = _cartProductCount
    private val _appliedPromo = mutableStateOf(false)
    val appliedPromo: State<Boolean> = _appliedPromo

    private val _cart = mutableStateOf<ApiResponse<CartResponse>>(ApiResponse.Idle())
    val cart: State<ApiResponse<CartResponse>> = _cart

    private val _checkoutResponse = mutableStateOf<ApiResponse<CheckoutResponse>>(ApiResponse.Idle())
    val checkoutResponse: State<ApiResponse<CheckoutResponse>> = _checkoutResponse

    private val _delivery = mutableStateOf<ApiResponse<Delivery>>(ApiResponse.Idle())
    val delivery: State<ApiResponse<Delivery>> = _delivery

    private val _selectedDelivery = mutableStateOf<DataDelivery?>(null)
    val selectedDelivery: State<DataDelivery?> = _selectedDelivery

    private val _countryState = mutableStateOf<ApiResponse<CountryState>>(ApiResponse.Idle())
    val countryState: State<ApiResponse<CountryState>> = _countryState

    private val _selectedCountry = mutableStateOf<Country?>(null)
    val selectedCountry: State<Country?> = _selectedCountry

    private val _selectedState = mutableStateOf<States?>(null)
    val selectedState: State<States?> = _selectedState

    private val _fullName = mutableStateOf("")
    val fullName: State<String> = _fullName
    private val _errorFullName = mutableStateOf(ErrorForm())
    val errorFullName: State<ErrorForm> = _errorFullName

    private val _zipCode = mutableStateOf("")
    val zipCode: State<String> = _zipCode
    private val _errorZipCode = mutableStateOf(ErrorForm())
    val errorZipCode: State<ErrorForm> = _errorZipCode

    private val _address = mutableStateOf("")
    val address: State<String> = _address
    private val _errorAddress = mutableStateOf(ErrorForm())
    val errorAddress: State<ErrorForm> = _errorAddress

    fun getDelivery(){
        _delivery.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _delivery.value = repository.getDelivery()
        }
    }

    fun getCountryState(){
        _countryState.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _countryState.value = repository.getCountryState()
        }
    }

    fun getCartData(data: List<OrderCart>){
        _cart.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _cart.value = repository.getCartData(data)
        }
    }

    fun updateProductCount(count: Int){
        _cartProductCount.value = count
    }

    fun onChangeFullName(it: String){
        checkFullNameForm(it)
        _fullName.value = it
    }

    fun checkFullNameForm(it: String){
        _errorFullName.value = ErrorForm()
        if(it.isEmpty()){
            _errorFullName.value = ErrorForm(true, "Name is empty")
        }else if(it.length < 4){
            _errorFullName.value = ErrorForm(true, "Min 4 characters")
        }
    }

    fun onChangeZipCode(it: String){
        checkZipForm(it)
        if(it.length <= 6) {
            _zipCode.value = it
        }
    }

    fun checkZipForm(it: String){
        _errorZipCode.value = ErrorForm()
        if(it.isEmpty()){
            _errorZipCode.value = ErrorForm(true, "Zipcode is empty")
        }else if(it.length < 4){
            _errorZipCode.value = ErrorForm(true, "Min 4 characters")
        }
    }

    fun onChangeAddress(it: String){
        checkAddressForm(it)
        _address.value = it
    }

    fun checkAddressForm(it: String){
        _errorAddress.value = ErrorForm()
        if(it.isEmpty()){
            _errorAddress.value = ErrorForm(true, "Address is empty")
        }else if(it.length < 15){
            _errorAddress.value = ErrorForm(true, "Min 15 characters")
        }
    }

    fun onChangeSelectedDelivery(data: DataDelivery){
        _selectedDelivery.value = data
    }

    fun updateUsePromo(promo: Boolean){
        _appliedPromo.value = promo
    }

    fun increaseItemCart(productId: String){
        viewModelScope.launch {
            cartRepository.increaseItem(productId)
        }
    }

    fun decreaseItemCart(productId: String){
        viewModelScope.launch {
            cartRepository.decreseItem(productId)
        }
    }

    fun onChangeCountry(country: Country){
        _selectedCountry.value = country
        _selectedState.value = null
    }

    fun onChangeState(state: States){
        _selectedState.value = state
    }

    fun deleteItemcart(productId: String){
        viewModelScope.launch {
            cartRepository.deleteFromCart(productId)
        }
    }

    fun checkAllForm(): Boolean{
        checkFullNameForm(_fullName.value)
        checkZipForm(_zipCode.value)
        checkAddressForm(_address.value)

        return !_errorAddress.value.error && !_errorFullName.value.error && !_errorZipCode.value.error
    }

    fun checkout(data: CheckoutForm, token: String){
        _checkoutResponse.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _checkoutResponse.value = repository.checkout(data, token)
        }
    }

    fun deleteAllCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCart()
        }
    }

}