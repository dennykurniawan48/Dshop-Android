package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listfavorite.ListFavorite
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.DetailProductRepository
import com.dennydev.dshop.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: FavoriteRepository
): ViewModel() {
    val token = datastoreRepository.flowToken
    private val _listFavorite = mutableStateOf<ApiResponse<ListFavorite>>(ApiResponse.Idle())
    val listFavorite: State<ApiResponse<ListFavorite>> = _listFavorite

    fun getListFavorite(token: String){
        _listFavorite.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _listFavorite.value = repository.getListFavorite(token)
        }
    }
}