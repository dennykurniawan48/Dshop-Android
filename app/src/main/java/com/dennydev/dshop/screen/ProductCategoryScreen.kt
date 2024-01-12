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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dennydev.dshop.R
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.EmptySearchResult
import com.dennydev.dshop.component.LoadingSearch
import com.dennydev.dshop.component.googleSignOut
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.ProductCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategory(
    navController: NavHostController,
    viewModel: ProductCategoryViewModel,
    id: String,
    name: String
) {
    val orderList = viewModel.orderItem.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(key1 = id){
        viewModel.loadItems(id)
    }

    Scaffold(modifier= Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = name) }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        } )
    }) { values ->
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier= Modifier
            .fillMaxSize()
            .padding(values)
            .padding(horizontal = 16.dp)){
            if (orderList.loadState.refresh is LoadState.NotLoading && orderList.itemCount > 0) {
                items(orderList.itemCount) {
                    val data = orderList.get(it)
                    data?.let {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ), modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(4.dp), onClick = {
                                navController.navigate(
                                    Screen.DetailProductScreen.route.replace(
                                        "{slug}",
                                        data.id
                                    )
                                )
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(context = context)
                                        .data(data.image1)
                                        .decoderFactory(SvgDecoder.Factory())
                                        .placeholder(R.drawable.placeholder)
                                        .build()
                                )
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    data.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(16.dp),
                                    maxLines = 2,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "",
                                        tint = Color.Yellow
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        Math.floor(
                                            ((data.averageRating
                                                ?: 0.0) * 100) / 100
                                        ).toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "$${data.price}",
                                        style = MaterialTheme.typography.labelLarge
                                    )
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

                    items(6) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .aspectRatio(1f)
                                    .background(
                                        shimmerEffect()
                                    )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .background(
                                        shimmerEffect()
                                    )
                            )
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Prev(
) {
    Scaffold(modifier= Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = "Profile") } )
    }) { values ->
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier= Modifier
            .padding(values)
            .padding(horizontal = 16.dp)){
            item(span = { GridItemSpan(2) }){
                Text("Computer", style=MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}