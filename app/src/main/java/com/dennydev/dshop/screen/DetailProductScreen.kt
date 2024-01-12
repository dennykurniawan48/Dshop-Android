package com.dennydev.dshop.screen

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.dennydev.dshop.R
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.DetailProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(
    navController: NavHostController,
    detailViewModel: DetailProductViewModel,
    id: String
) {
    val token by detailViewModel.token.collectAsState(initial = "")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val detail by detailViewModel.detailProduct
    val review by detailViewModel.review
    val inFav by detailViewModel.isInFavorite
    val isInFav by detailViewModel.productInFav
    val itemExist by detailViewModel.itemExist
    val snackbarHostState = remember{SnackbarHostState()}
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = token) {
        if (token.isNotEmpty()) {
            detailViewModel.checkItemInDatabase(id)
            detailViewModel.getDetailProduct(token, id)
        }
    }

    LaunchedEffect(key1 = inFav) {
        inFav.data?.data?.let {
            detailViewModel.onChangeProductFav(it.isExist)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(), snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, bottomBar = {
        BottomAppBar() {
            detail.data?.data?.let {
                Column(
                    modifier = Modifier.fillMaxWidth(.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$${it.price}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(onClick = {
                    if (!itemExist) {
                        detailViewModel.addItemToCart(id)
                    }
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "item added to cart", duration = SnackbarDuration.Short
                        )
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Add cart", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }, topBar = {
        TopAppBar(title = { }, navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIos,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }, scrollBehavior = scrollBehavior, actions = {
            if (isInFav) {
                IconButton(onClick = {
                    detailViewModel.removeFromFav(id, token)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "in fav",
                        tint = Color.Red,
                    )
                }
            } else {
                IconButton(onClick = {
                    detailViewModel.addToFavorite(id, token)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "not in fav",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        })
    }) { values ->
        LazyColumn(modifier = Modifier.padding(values)) {
            if (detail is ApiResponse.Loading) {
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(shimmerEffect())
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .height(48.dp)
                            .width(170.dp)
                            .padding(12.dp)
                            .background(shimmerEffect())
                    )
                }
            }

            if (review is ApiResponse.Loading) {
                item {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(36.dp)
                            .width(160.dp)
                            .background(shimmerEffect())
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(36.dp)
                            .width(160.dp)
                            .background(shimmerEffect())
                    )
                }
            }

            if (detail is ApiResponse.Loading) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(
                        modifier = Modifier
                            .height(24.dp)
                            .width(150.dp)
                            .background(shimmerEffect())
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Spacer(
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()
                            .background(shimmerEffect())
                    )
                }
            }

            if (review is ApiResponse.Loading) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(
                        modifier = Modifier
                            .width(200.dp)
                            .height(120.dp)
                            .background(shimmerEffect())
                    )
                }
            }

            detail.data?.data?.let {
                item {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.image1),
                        contentDescription = "Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                item {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                review.data?.data?.total?.let { total ->
                    detail.data?.data?.averageRating?.let { averageRating ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AssistChip(
                                onClick = { /*TODO*/ },
                                label = { Text(text = "%.1f".format(averageRating)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "",
                                        tint = Color.Yellow
                                    )
                                },
                                modifier = Modifier.padding(start = 12.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("(${total}) reviews", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
            item {
                detail.data?.data?.let {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Available stock: ${it.availableStock}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                detail.data?.data?.let {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Product description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(it.desc, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            item {
                review.data?.data?.let { review ->
                    Column(Modifier.padding(12.dp)) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Rating",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        review.star.forEach { star ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    star.rating.toString(),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LinearProgressIndicator(
                                    progress = (star.count.toDouble() / if (review.total == 0) 1 else review.total).toFloat(),
                                    modifier = Modifier.fillMaxWidth(.5f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    star.count.toString(),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
            item {
                review.data?.data?.let {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            "Newest review",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        it.review.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxWidth(0.2f)
                                        .aspectRatio(1f)
                                )
                                Column(verticalArrangement = Arrangement.Center) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            it.user.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "",
                                            tint = Color.Yellow
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            it.rating.toString(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        it.comment,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun PrevDetProduct() {
    Scaffold(modifier = Modifier
        .fillMaxSize(), bottomBar = {
        BottomAppBar() {
            Column(
                modifier = Modifier.fillMaxWidth(.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$75.67",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Add cart", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }) { values ->
        LazyColumn(modifier = Modifier.padding(bottom = values.calculateBottomPadding())) {
            item {
                Image(
                    painter = rememberAsyncImagePainter(model = ""),
                    contentDescription = "Image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "PC EVOS 1107",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = { /*TODO*/ },
                        label = { Text(text = "4.8") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "",
                                tint = Color.Yellow
                            )
                        },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("(5) reviews", style = MaterialTheme.typography.titleMedium)
                }
            }
            item {
                Row(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Available stock: 5",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item {
                Row(modifier = Modifier.padding(12.dp)) {
                    //    Text("orem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", style = MaterialTheme.typography.bodyLarge)
                }
            }
            item {
//                Column(Modifier.padding(12.dp)) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text("5")
//                        Spacer(modifier=Modifier.width(8.dp))
//                        LinearProgressIndicator(progress = (4f/5), modifier=Modifier.fillMaxWidth(.5f))
//                        Spacer(modifier=Modifier.width(8.dp))
//                        Text("4")
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text("4")
//                        Spacer(modifier=Modifier.width(8.dp))
//                        LinearProgressIndicator(progress = 1f/5, modifier=Modifier.fillMaxWidth(.5f))
//                        Spacer(modifier=Modifier.width(8.dp))
//                        Text("1")
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text("3")
//                        Spacer(modifier=Modifier.width(8.dp))
//                        LinearProgressIndicator(progress = 0f/5, modifier=Modifier.fillMaxWidth(.5f))
//                        Spacer(modifier=Modifier.width(8.dp))
//                        Text("0")
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text("2")
//                        Spacer(modifier=Modifier.width(8.dp))
//                        LinearProgressIndicator(progress = 0f/5, modifier=Modifier.fillMaxWidth(.5f))
//                        Spacer(modifier=Modifier.width(8.dp))
//                        Text("0")
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text("1")
//                        Spacer(modifier=Modifier.width(8.dp))
//                        LinearProgressIndicator(progress = 0f/5, modifier=Modifier.fillMaxWidth(.5f))
//                        Spacer(modifier=Modifier.width(8.dp))
//                        Text("0")
//                    }
//                }
            }
            item {
                Column(Modifier.padding(12.dp)) {
                    Text("Newest review", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .aspectRatio(1f)
                        )
                        Column(verticalArrangement = Arrangement.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Test Account", style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    tint = Color.Yellow
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("5", style = MaterialTheme.typography.bodyLarge)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Great product", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .aspectRatio(1f)
                        )
                        Column(verticalArrangement = Arrangement.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Test Account", style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "",
                                    tint = Color.Yellow
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("5", style = MaterialTheme.typography.bodyLarge)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Great product", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

        }
    }
}