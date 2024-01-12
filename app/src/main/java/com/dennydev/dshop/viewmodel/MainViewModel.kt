package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennydev.dshop.dao.OrderCart
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listcategory.ListCategory
import com.dennydev.dshop.model.productresponse.Product
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.sliderresponse.SliderResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.CartOrderRepository
import com.dennydev.dshop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: MainRepository,
    private val cartRepository: CartOrderRepository
): ViewModel() {
    val token = datastoreRepository.flowToken
    private val _selectedBottomNav: MutableState<String> = mutableStateOf(Screen.HomeScreen.route)
    val selecteBottomNav: State<String> = _selectedBottomNav

    private val _querySearch: MutableState<String> = mutableStateOf("")
    val querySearch: State<String> = _querySearch

    private val _searchItems = MutableStateFlow<PagingData<Product>>(PagingData.empty(sourceLoadStates = LoadStates(refresh = LoadState.NotLoading(endOfPaginationReached = false), prepend = LoadState.NotLoading(endOfPaginationReached = false), append = LoadState.NotLoading(endOfPaginationReached = false))))
    val searchItems: StateFlow<PagingData<Product>> = _searchItems

    private val _fetchHomePage: MutableState<Boolean> = mutableStateOf(false)
    val fetchHomePage: State<Boolean> = _fetchHomePage

    private val _slider: MutableState<ApiResponse<SliderResponse>> = mutableStateOf(ApiResponse.Idle())
    val slider: State<ApiResponse<SliderResponse>> = _slider

    private val _categories: MutableState<ApiResponse<ListCategory>> = mutableStateOf(ApiResponse.Idle())
    val categories: State<ApiResponse<ListCategory>> = _categories

    private val _newestProduct: MutableState<ApiResponse<ProductResponse>> = mutableStateOf(ApiResponse.Idle())
    val newestProduct: State<ApiResponse<ProductResponse>> = _newestProduct

    private val _homeScrollPosition = mutableStateOf(0)
    val homeScrollPosition = _homeScrollPosition

    private val _itemsCart = MutableStateFlow<List<OrderCart>>(emptyList())
    val itemsCart: StateFlow<List<OrderCart>> = _itemsCart

    fun getAllCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.cartData.collectLatest {
                _itemsCart.value = it
            }
        }
    }

    fun onChangeSelectedBottomNav(route: String){
        _selectedBottomNav.value = route
    }

    fun onChangeScrollPosition(newPos: Int){
        _homeScrollPosition.value = newPos
    }

    fun loadItems(queryParam: String) {
        if(queryParam.isEmpty()){
            _searchItems.value = PagingData.empty()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProduct(queryParam)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _searchItems.value = pagingData
                }
        }
    }

    fun onChangeQuery(query: String){
        _querySearch.value = query
    }

    fun onHomepageLoad(){
        _fetchHomePage.value = true
    }

    fun getSlider(){
        _slider.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _slider.value = repository.getSlider()
        }
    }

    fun getCategory(){
        _categories.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _categories.value = repository.getCategory()
        }
    }

    fun getNewestProduct(){
        _newestProduct.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _newestProduct.value = repository.getNewestProduct()
        }
    }
}