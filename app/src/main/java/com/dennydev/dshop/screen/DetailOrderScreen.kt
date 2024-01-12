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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.component.shimmerEffect
import com.dennydev.dshop.helper.IsoToDateString
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.DetailOrderViewModel
import com.dennydev.dshop.viewmodel.DetailProductViewModel
import com.dennydev.dshop.viewmodel.MainViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailOrderScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    detailOrderViewModel: DetailOrderViewModel,
    id: String
) {
    val token by mainViewModel.token.collectAsState(initial = "")
    val detailOrder by detailOrderViewModel.detailProduct
    val paymentSheet =
        rememberPaymentSheet(paymentResultCallback = detailOrderViewModel::onPaymentSheetResult)
    val paymentSheetData by detailOrderViewModel.paymentSheetdata
    val checkoutResult by detailOrderViewModel.checkoutState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = checkoutResult){
        if(checkoutResult == PaymentSheetResult.Completed){
            detailOrderViewModel.getDetailProduct(id)
            navController.navigate(Screen.PaymentSuccessScreen.route)
            detailOrderViewModel.resetPaymentSheet()
        }
    }

    LaunchedEffect(key1 = paymentSheetData) {
        if (paymentSheetData is ApiResponse.Success) {
            paymentSheetData.data?.data?.let {
                val configuration = PaymentSheet.CustomerConfiguration(
                    it.customer, it.ephemeralKey
                )
                PaymentConfiguration.init(context, it.publishableKey)
                paymentSheet.presentWithPaymentIntent(
                    it.paymentIntent, PaymentSheet.Configuration(
                        "Dshop", customer = configuration
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = token){
        if(token.isNotEmpty()){
            detailOrderViewModel.getDetailProduct(id)
        }
    }

    Scaffold(modifier= Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { Text(text = "Detail Order")}, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "")
            }
        })
    }, bottomBar = {
        detailOrder.data?.data?.let{
            if(!it.paid){
                    Button(onClick = {
                        detailOrderViewModel.getPaymentSheetData(
                            it.total, token, it.id
                        )
                    }, modifier= Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)) {
                        Text(if(paymentSheetData is ApiResponse.Loading) "Loading..." else "Pay $${it.total}")
                    }
            }
        }
    }) { values ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            ) {
                if(detailOrder !is ApiResponse.Success){
                    item {
                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .height(40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier= Modifier
                                .height(20.dp)
                                .width(50.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier= Modifier
                                .height(20.dp)
                                .width(70.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }

                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .height(40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier= Modifier
                                .height(20.dp)
                                .width(70.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier= Modifier
                                .height(20.dp)
                                .width(120.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }
                        Spacer(modifier=Modifier.height(36.dp))
                        Spacer(modifier= Modifier
                            .height(24.dp)
                            .width(100.dp)
                            .background(shimmerEffect()))
                        Spacer(modifier=Modifier.height(12.dp))
                        Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(0.15f)
                                    .aspectRatio(1f)
                                    .background(shimmerEffect())
                            )
                            Spacer(modifier=Modifier.width(8.dp))
                            Column() {
                                Spacer(modifier= Modifier
                                    .width(150.dp)
                                    .height(30.dp)
                                    .background(
                                        shimmerEffect()
                                    ))
                                Spacer(modifier=Modifier.height(4.dp))
                                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                    Spacer(modifier= Modifier
                                        .width(100.dp)
                                        .height(30.dp)
                                        .background(
                                            shimmerEffect()
                                        ))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Spacer(modifier=Modifier.height(36.dp))
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(100.dp)
                                    .background(
                                        shimmerEffect()
                                    )
                            )

                            Spacer(modifier=Modifier.width(20.dp))
                            Spacer(modifier = Modifier
                                .width(210.dp)
                                .height(60.dp)
                                .background(
                                    shimmerEffect()
                                )
                            )
                        }
                        Spacer(modifier=Modifier.height(12.dp))
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier
                                .width(100.dp)
                                .height(30.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier=Modifier.width(20.dp))
                            Spacer(modifier = Modifier
                                .width(210.dp)
                                .height(60.dp)
                                .background(
                                    shimmerEffect()
                                )
                            )
                        }

                        Spacer(modifier=Modifier.height(36.dp))
                        Spacer(modifier= Modifier
                            .height(20.dp)
                            .width(100.dp)
                            .background(
                                shimmerEffect()
                            ))
                        Spacer(modifier=Modifier.height(12.dp))
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(100.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(60.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }
                        Spacer(modifier=Modifier.height(12.dp))

                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(100.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(60.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }
                        Spacer(modifier=Modifier.height(12.dp))

                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(100.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier = Modifier
                                .height(20.dp)
                                .width(60.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }
                        Spacer(modifier=Modifier.height(12.dp))

                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier
                                .height(40.dp)
                                .width(100.dp)
                                .background(
                                    shimmerEffect()
                                ))
                            Spacer(modifier = Modifier
                                .height(40.dp)
                                .width(120.dp)
                                .background(
                                    shimmerEffect()
                                ))
                        }
                        Spacer(modifier=Modifier.height(12.dp))
                    }
                }
                detailOrder.data?.data?.let {detail  ->
                item {
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(detail.status.status_name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = {

                        }) {
                            Text("See Detail", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                item {
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Order Date", style = MaterialTheme.typography.bodyMedium)
                        Text(IsoToDateString(detail.createdAt), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                item {
                    Spacer(modifier=Modifier.height(36.dp))
                    Text("Detail", style=MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier=Modifier.height(12.dp))
                }
                items(detail.detailorder.size){ index ->
                    val data = detail.detailorder.get(index)
                    Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                        Image(
                            painter = rememberAsyncImagePainter(model = data.products.image1),
                            contentDescription = "Image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth(0.15f)
                                .aspectRatio(1f)
                        )
                        Spacer(modifier=Modifier.width(8.dp))
                        Column() {
                            Text(data.products.name, style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier=Modifier.height(4.dp))
                            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                Text("${data.qty} x $${data.price}", style=MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                item {
                    Spacer(modifier=Modifier.height(36.dp))
                    Text("Shipping info", style=MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Text("Delivery type", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.35f))
                            Text(detail.delivery.name, style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth())
                        }
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Text("Address", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.35f))
                        Column(){
                            Text(detail.fullName, style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth())
                            Text(detail.address, style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth())
                            Text("${detail.state.name}, ${detail.state.country.name}", style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
                item {
                    Spacer(modifier=Modifier.height(36.dp))
                    Text("Payment summary", style=MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Text("Metode Pembayaran", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.65f))
                        Text("Stripe", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Text("Discount", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.65f))
                        Text("$${detail.discount}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Text("Delivery cost", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.65f))
                        Text("$${detail.deliveryCost}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                }
                item{
                    Divider()
                    Spacer(modifier=Modifier.height(12.dp))
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Text("Total", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.65f))
                        Text("$${detail.total}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}