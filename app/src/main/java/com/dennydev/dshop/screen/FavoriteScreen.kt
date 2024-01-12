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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dennydev.dshop.R
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.FavoriteViewModel
import com.dennydev.dshop.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    favoriteViewModel: FavoriteViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBottomNavigationItem by mainViewModel.selecteBottomNav
    val token by favoriteViewModel.token.collectAsState(initial = "")
    val listFavorite by favoriteViewModel.listFavorite
    val context = LocalContext.current

    LaunchedEffect(key1 = token) {
        if (token.isNotEmpty()) {
            favoriteViewModel.getListFavorite(token)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(title = { Text("Wishlist") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBackIos, contentDescription = "")
            }
        }, scrollBehavior = scrollBehavior)
    }, bottomBar = {
        BottomNav(
            navController = navController,
            selectedBottomIndex = selectedBottomNavigationItem,
            onChangeSelectedBottomIndex = {
                mainViewModel.onChangeSelectedBottomNav(it)
            })
    }) { values ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
                .padding(vertical = 12.dp, horizontal = 16.dp), columns = GridCells.Fixed(2)
        ) {
            if(listFavorite is ApiResponse.Loading) {
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
            listFavorite.data?.data?.let { favorites ->
                items(favorites.size) { index ->
                    val data = favorites.get(index)
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
                                    data.productId
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
                                    .data(data.product.image1)
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
                                data.product.name,
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
                                        ((data.product.averageRating
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
                                    text = "$${data.product.price}",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}