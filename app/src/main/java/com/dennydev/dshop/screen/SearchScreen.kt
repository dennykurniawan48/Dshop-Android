package com.dennydev.dshop.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.dennydev.dshop.component.ErrorSearch
import com.dennydev.dshop.component.LoadingSearch
import com.dennydev.dshop.component.SearchTopBar
import com.dennydev.dshop.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController,mainViewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBottomNavigationItem by mainViewModel.selecteBottomNav
    val query by mainViewModel.querySearch
    val productList = mainViewModel.searchItems.collectAsLazyPagingItems()
    val context = LocalContext.current
    Scaffold(modifier= Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        SearchTopBar(query = query, onSearch = {
            mainViewModel.loadItems(query)
        }, onClose = { navController.popBackStack() }, onChangeQuery = {
            mainViewModel.onChangeQuery(it)
        })
    }, bottomBar = { BottomNav(navController= navController,selectedBottomIndex = selectedBottomNavigationItem, onChangeSelectedBottomIndex = {
        mainViewModel.onChangeSelectedBottomNav(it)
    }) }){ values ->
        Column(modifier= Modifier
            .fillMaxSize()
            .padding(values), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            // Log.d("paging", productList.loadState.toString())
            // use above Log.d function to debug which state is correct for different state of pagination : Denny Kurniawan
            if (productList.loadState.refresh is LoadState.NotLoading && productList.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(productList.itemCount) { item ->
                        val data = productList.get(item)
                        data?.let {
                            Spacer(modifier=Modifier.height(4.dp))
                            Row(modifier = Modifier
                                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(it.image1)
                                        .crossfade(true)
                                        .build(),
                                    placeholder = painterResource(R.drawable.placeholder),
                                    contentDescription = "",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth(0.2f)
                                        .aspectRatio(1f)
                                )
                                Spacer(modifier=Modifier.width(6.dp))
                                Column(modifier=Modifier.fillMaxWidth(0.7f)) {
                                    Text(text = it.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text="$${it.price}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    item{
                        if(productList.loadState.append is LoadState.Loading && !productList.loadState.append.endOfPaginationReached){
                            LoadingSearch()
                        }
                    }
                }
            }else if (productList.loadState.refresh is LoadState.NotLoading && productList.loadState.append is LoadState.NotLoading && !productList.loadState.refresh.endOfPaginationReached && productList.itemCount == 0) {
                EmptySearchResult()
            } else if (productList.loadState.refresh is LoadState.NotLoading && productList.loadState.append is LoadState.Loading && !productList.loadState.refresh.endOfPaginationReached && productList.itemCount == 0) {
                Text("Empty result")
            } else if (productList.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}