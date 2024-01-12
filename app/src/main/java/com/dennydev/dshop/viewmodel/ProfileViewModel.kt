package com.dennydev.dshop.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listorder.Order
import com.dennydev.dshop.model.logoutresponse.LogoutResponse
import com.dennydev.dshop.profileresponse.ProfileResponse
import com.dennydev.dshop.repository.AuthStoreRepository
import com.dennydev.dshop.repository.ProfileRepository
import com.google.android.gms.tasks.OnCompleteListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val datastoreRepository: AuthStoreRepository,
    private val repository: ProfileRepository
): ViewModel() {
    private val _profile = mutableStateOf<ApiResponse<ProfileResponse>>(ApiResponse.Idle())
    val profile: State<ApiResponse<ProfileResponse>> = _profile

    private val _orderItem = MutableStateFlow<PagingData<Order>>(PagingData.empty(sourceLoadStates = LoadStates(refresh = LoadState.NotLoading(endOfPaginationReached = false), prepend = LoadState.NotLoading(endOfPaginationReached = false), append = LoadState.NotLoading(endOfPaginationReached = false))))
    val orderItem: StateFlow<PagingData<Order>> = _orderItem

    val isGoogle = datastoreRepository.flowGoogle
    private val _logoutResponse = mutableStateOf<ApiResponse<LogoutResponse>>(ApiResponse.Idle())
    val logoutResponse: State<ApiResponse<LogoutResponse>> = _logoutResponse

    fun getProfile(token: String){
        _profile.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _profile.value = repository.getProfile(token)
        }
    }

    fun loadItems(token: String) {
        viewModelScope.launch {
            repository.getProfileOrder(token)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _orderItem.value = pagingData
                }
        }
    }

    fun logout(token: String){
        viewModelScope.launch {
            _logoutResponse.value = repository.logout(token)
            datastoreRepository.removeToken()
        }
    }
}