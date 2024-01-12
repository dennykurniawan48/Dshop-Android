package com.dennydev.dshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennydev.dshop.model.productcategory.Product
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.ProductCategoryRepository
import com.dennydev.dshop.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: ProductCategoryRepository
): ViewModel() {
    private val _orderItem = MutableStateFlow<PagingData<Product>>(PagingData.empty(sourceLoadStates = LoadStates(refresh = LoadState.NotLoading(endOfPaginationReached = false), prepend = LoadState.NotLoading(endOfPaginationReached = false), append = LoadState.NotLoading(endOfPaginationReached = false))))
    val orderItem: StateFlow<PagingData<Product>> = _orderItem

    fun loadItems(idCategory: String) {
        viewModelScope.launch {
            repository.getProductByCategory(idCategory)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _orderItem.value = pagingData
                }
        }
    }
}