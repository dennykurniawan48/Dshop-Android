package com.dennydev.dshop.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dennydev.dshop.R
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun HomeScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val selectedBottomNavigationItem by mainViewModel.selecteBottomNav
    val fetchHomeScreen by mainViewModel.fetchHomePage
    val slider by mainViewModel.slider
    val categories by mainViewModel.categories
    val newestProduct by mainViewModel.newestProduct
    val context = LocalContext.current
    val state = rememberPagerState { slider.data?.data?.size ?: 0 }
    var lastPosition by mainViewModel.homeScrollPosition

    val brush = shimmerEffect()

//    val listState = rememberLazyGridState(
//        initialFirstVisibleItemIndex = lastPosition,
//        initialFirstVisibleItemScrollOffset = lastPosition
//    )
    val itemInCart by mainViewModel.itemsCart.collectAsState()
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

//    LaunchedEffect(key1 = lastPosition) {
//        mainViewModel.onChangeScrollPosition(lastPosition)
//    }

    LaunchedEffect(key1 = fetchHomeScreen) {
        if (!fetchHomeScreen) {
            mainViewModel.getSlider()
            mainViewModel.getCategory()
            mainViewModel.getNewestProduct()
            mainViewModel.onHomepageLoad()
        }
    }
//
//    LaunchedEffect(key1 = listState) {
//        snapshotFlow {
//            listState.firstVisibleItemIndex
//        }
//            .debounce(500L)
//            .collectLatest { index ->
//                lastPosition = index
//            }
//    }

    LaunchedEffect(key1 = Unit) {
        mainViewModel.getAllCart()
    }

    LaunchedEffect(key1 = itemInCart) {
        Log.d("Items", itemInCart.toString())
    }

    LaunchedEffect(state.pageCount) {
        while (state.pageCount != 0) {
            delay(3000)

            state.animateScrollToPage(if (state.pageCount == 0) 0 else (state.currentPage + 1) % state.pageCount)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(title = { Text(text = "Dshop") }, scrollBehavior = scrollBehavior, actions = {

            IconButton(onClick = { navController.navigate(Screen.CartScreen.route) }) {
                BadgedBox(
                    badge = {
                        if (itemInCart.isNotEmpty()) {
                            Badge()
                        }
                    }) {
                    Icon(imageVector = Icons.Outlined.ShoppingBag, contentDescription = "cart")
                }
            }

        })
    }, bottomBar = {
        BottomNav(
            navController = navController,
            selectedBottomIndex = selectedBottomNavigationItem,
            onChangeSelectedBottomIndex = {
                mainViewModel.onChangeSelectedBottomNav(it)
            })
    }) { values ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
//            state = listState,
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(scrollState)
                .padding(values)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.SearchScreen.route)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Search",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                }
            }
            item(span = { GridItemSpan(2) }) {
                if(!(slider is ApiResponse.Success)){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(0.dp, 20.dp, 0.dp, 20.dp)
                        .background(
                            brush
                        )
                    ){
                    }
                }
                slider.data?.data?.let {
                    HorizontalPager(
                        state = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(0.dp, 20.dp, 0.dp, 20.dp)
                    ) { page ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 0.dp, 15.dp, 0.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = it[page].image),
                                contentDescription = "Image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                if(!(categories is ApiResponse.Success)){
                    Row(modifier=Modifier.fillMaxWidth()){
                        Spacer(modifier= Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(brush)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Spacer(modifier= Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(brush)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Spacer(modifier= Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(brush)
                        )
                    }
                }
                categories.data?.let {
                    LazyRow(modifier = Modifier.padding(8.dp)) {
                        items(it.data.size) { colIndex ->
                            FilterChip(
                                selected = false,
                                onClick = {
                                    navController.navigate(Screen.ProductCategoryScreen.route.replace("{id}", it.data.get(colIndex).id).replace("{name}", it.data.get(colIndex).name))
                                }, label = {
                                    Text(it.data.get(colIndex).name)
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                if(!(newestProduct is ApiResponse.Success)){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Spacer(modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(brush)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                newestProduct.data?.data?.let { dataProduct ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Newest Product", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if(newestProduct !is ApiResponse.Success){
                items(4){
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .aspectRatio(1f)
                            .background(
                                brush
                            )
                        )
                        Spacer(modifier=Modifier.height(16.dp))
                        Spacer(modifier= Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .background(
                                brush
                            )
                        )
                    }
                }
            }

            newestProduct.data?.data?.let { dataProduct ->
                items(dataProduct.products.size) { productIndex ->
                    val data = dataProduct.products.get(productIndex)
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
        }
    }
}