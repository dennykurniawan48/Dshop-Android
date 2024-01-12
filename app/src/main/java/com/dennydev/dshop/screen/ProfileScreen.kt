package com.dennydev.dshop.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dennydev.dshop.R
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.EmptySearchResult
import com.dennydev.dshop.component.LoadingSearch
import com.dennydev.dshop.component.googleSignOut
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.helper.IsoToDateString
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.MainViewModel
import com.dennydev.dshop.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel, profileViewModel: ProfileViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBottomNavigationItem by mainViewModel.selecteBottomNav
    val token by mainViewModel.token.collectAsState(initial = "")
    val profile by profileViewModel.profile
    val orderList = profileViewModel.orderItem.collectAsLazyPagingItems()
    val context = LocalContext.current
    val isGoogleLogin by profileViewModel.isGoogle.collectAsState(initial = false)
    val logoutResponse by profileViewModel.logoutResponse

    LaunchedEffect(key1 = token, key2 = orderList){
        if(token.isNotEmpty()){
            profileViewModel.getProfile(token)
            profileViewModel.loadItems(token)
        }
    }

    LaunchedEffect(key1 = logoutResponse){
        if(logoutResponse is ApiResponse.Success){
            navController.navigate(Screen.LoginScreen.route){
                popUpTo(Screen.LoginScreen.route){
                    inclusive=true
                }
            }
        }
    }

    Scaffold(modifier= Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(title = { Text(text = "Profile") }, scrollBehavior=scrollBehavior, actions = {
            IconButton(onClick = {
                if(isGoogleLogin){
                    googleSignOut(context = context){
                        profileViewModel.logout(token)
                    }
                }else{
                    profileViewModel.logout(token)
                }
            }) {
                Icon(imageVector = Icons.Outlined.Logout, contentDescription = "logout")
            }
        })
    }, bottomBar = { BottomNav(navController= navController,selectedBottomIndex = selectedBottomNavigationItem, onChangeSelectedBottomIndex = {
        mainViewModel.onChangeSelectedBottomNav(it)
    }) }){ values ->
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .padding(values), horizontalAlignment = Alignment.CenterHorizontally){

            if(profile is ApiResponse.Loading){
                item {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .clip(shape = CircleShape)
                    )
                }
                item{
                    Spacer(modifier= Modifier
                        .width(70.dp)
                        .height(30.dp)
                        .background(shimmerEffect()))
                }
                item{
                    Spacer(modifier=Modifier.height(4.dp))
                    Spacer(modifier= Modifier
                        .width(120.dp)
                        .height(24.dp)
                        .background(shimmerEffect()))
                }
            }

            profile.data?.data?.let {profile ->
                item {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .clip(shape = CircleShape)
                    )
                }
                item{
                    Text(profile.name, style = MaterialTheme.typography.titleMedium)
                }
                item{
                    Spacer(modifier=Modifier.height(4.dp))
                    Text(profile.email, style = MaterialTheme.typography.labelMedium)
                }
                if (orderList.loadState.refresh is LoadState.NotLoading && orderList.itemCount > 0) {

                    item {
                        Spacer(modifier=Modifier.height(24.dp))
                        Text("Past Order", style = MaterialTheme.typography.titleMedium)
                    }
                    items(orderList.itemCount) { item ->
                            val data = orderList.get(item)
                            data?.let {
                                Spacer(modifier=Modifier.height(4.dp))
                                Card(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp), ){
                                    Column(
                                        modifier= Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        val item = data.detailorder.get(0)
                                        val moreThanOne = data.detailorder.size - 1
                                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                                            Column {
                                                Text(IsoToDateString(data.createdAt), style = MaterialTheme.typography.labelMedium)
                                            }
                                            Text(data.status.status_name, style = MaterialTheme.typography.labelMedium)
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(item.products.image1)
                                                    .crossfade(true)
                                                    .build(),
                                                placeholder = painterResource(R.drawable.placeholder),
                                                contentDescription = "",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth(0.2f)
                                                    .aspectRatio(1f)
                                            )
                                            Spacer(modifier=Modifier.width(8.dp))
                                            Column() {
                                                Text(item.products.name, style = MaterialTheme.typography.bodyMedium)
                                                Spacer(modifier=Modifier.height(8.dp))
                                                Text("${item.qty} x $${item.price}", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }

                                        if(moreThanOne > 0){
                                            Row(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp), horizontalArrangement = Arrangement.Center){
                                                Text("${moreThanOne} more item")
                                            }
                                        }
                                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                                            Button(onClick = {
                                                navController.navigate(Screen.DetailOrderScreen.route.replace("{slug}", data.id))
                                            }) {
                                                Text("Detail")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item{
                            if(orderList.loadState.append is LoadState.Loading && !orderList.loadState.append.endOfPaginationReached){
                                LoadingSearch()
                            }
                        }
                }else if (orderList.loadState.refresh is LoadState.NotLoading && orderList.loadState.append is LoadState.NotLoading && !orderList.loadState.refresh.endOfPaginationReached && orderList.itemCount == 0) {
                    item {
                        Spacer(modifier=Modifier.height(24.dp))
                        EmptySearchResult()
                    }
                } else if (orderList.loadState.refresh is LoadState.NotLoading && orderList.loadState.append is LoadState.Loading && !orderList.loadState.refresh.endOfPaginationReached && orderList.itemCount == 0) {
                    item {
                        Spacer(modifier=Modifier.height(48.dp))
                        Text("Empty result")
                    }
                } else if (orderList.loadState.refresh is LoadState.Loading) {
                    item{
                        Spacer(modifier=Modifier.height(48.dp))
                        CircularProgressIndicator()
                    }
                }

            }
        }
    }
}