package com.dennydev.dshop.screen

import android.util.Log
import android.widget.RadioGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dennydev.dshop.component.BottomNav
import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.form.checkout.CheckoutForm
import com.dennydev.dshop.model.form.checkout.Detail
import com.dennydev.dshop.navigation.Screen
import com.dennydev.dshop.viewmodel.CartViewModel
import com.dennydev.dshop.viewmodel.MainViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    cartViewModel: CartViewModel,
    usePromo: Boolean
) {
    val token by mainViewModel.token.collectAsState(initial = "")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val productCount by cartViewModel.cartProductCount
    val cartData by cartViewModel.cart

    var subtotal by rememberSaveable { mutableStateOf(0.00) }
    val applyPromo by cartViewModel.appliedPromo
    var discount by rememberSaveable { mutableStateOf(0.00) }
    val delivery by cartViewModel.delivery
    val selectedDelivery by cartViewModel.selectedDelivery
    var finalTotal by rememberSaveable { mutableStateOf(0.00) }
    var isQtyOrderError by rememberSaveable { mutableStateOf(false) }
    val countryState by cartViewModel.countryState

    val checkoutResponse by cartViewModel.checkoutResponse
    val context = LocalContext.current
    val selectedCountry by cartViewModel.selectedCountry
    val selectedState by cartViewModel.selectedState
    val fullName by cartViewModel.fullName
    val errorFullName by cartViewModel.errorFullName
    val zipCode by cartViewModel.zipCode
    val errorZipCode by cartViewModel.errorZipCode
    val address by cartViewModel.address
    val errorAddress by cartViewModel.errorAddress

    var expandedCountry by remember { mutableStateOf(false) }
    var expandedState by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = usePromo) {
        cartViewModel.updateUsePromo(usePromo)
    }
    
    LaunchedEffect(key1 = checkoutResponse){
        if(checkoutResponse is ApiResponse.Success) {
            checkoutResponse.data?.data?.let {
                cartViewModel.deleteAllCart()
                navController.popBackStack()
                navController.navigate(Screen.DetailOrderScreen.route.replace("{slug}", it.id))
            }
        }
    }

    LaunchedEffect(key1 = token) {
        if (token.isNotEmpty()) {
            cartViewModel.getDelivery()
            cartViewModel.getCountryState()
        }
    }

    LaunchedEffect(key1 = cartItems) {
        cartViewModel.updateProductCount(cartItems.size)
    }

    LaunchedEffect(key1 = cartItems, cartData) {
        isQtyOrderError = false
        cartData.data?.data?.forEach { data ->
            cartItems.forEach { item ->
                var qtyInCart = 1
                try {
                    val itemInCart = cartItems.first { it.productId == data.id }
                    qtyInCart = itemInCart.qty
                } catch (e: Exception) {

                }
                if (qtyInCart > data.availableStock) {
                    isQtyOrderError = true
                }
            }
        }
    }

    LaunchedEffect(key1 = productCount) {
        cartViewModel.getCartData(cartItems)
    }

    LaunchedEffect(cartData, cartItems, applyPromo, selectedDelivery) {
        subtotal = 0.00
        cartData.data?.data?.let { data ->
            data.forEach { item ->
                var qtyInCart = 1
                try {
                    val itemInCart = cartItems.first { it.productId == item.id }
                    qtyInCart = itemInCart.qty
                } catch (e: Exception) {

                }
                val totalItem = item.price * qtyInCart
                subtotal += totalItem
            }
        }
        discount = if (applyPromo) subtotal / 2 else 0.00
        finalTotal = subtotal - discount + (selectedDelivery?.price ?: 0.00)
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(title = { Text(text = "Cart") }, scrollBehavior = scrollBehavior)
    }, bottomBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Promo",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(0.4f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (applyPromo) {
                        TextButton(onClick = {
                            navController.navigate(Screen.PromoScreen.route)
                        }) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("50% Discount", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        TextButton(onClick = {
                            navController.navigate(Screen.PromoScreen.route)
                        }) {
                            Text("See promo", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Subtotal",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${Math.floor(subtotal * 100) / 100}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Discount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${Math.floor(discount * 100) / 100}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Delivery fee",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${Math.floor((selectedDelivery?.price ?: 0.00) * 100) / 100}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (cartViewModel.checkAllForm()) {
                        val detail: List<Detail> = cartItems.map { d ->
                            Detail(productId = d.productId, qty = d.qty)
                        }
                        cartViewModel.checkout(
                            CheckoutForm(
                                address = address,
                                coupon = if (usePromo) "PORTFOLIO50" else "",
                                deliveryCost = selectedDelivery?.price ?: 0.0,
                                details = detail,
                                shippingMethodId = selectedDelivery?.id ?: "",
                                discount = Math.floor(discount*100)/100,
                                stateId = selectedState?.id ?: "",
                                total = Math.floor(finalTotal*100)/100,
                                zipCode = zipCode
                            ), token
                        )
                    }

                },
                enabled = (!isQtyOrderError && !errorAddress.error && !errorZipCode.error && !errorFullName.error && cartItems.isNotEmpty() && selectedDelivery != null && selectedState != null),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Checkout for $${Math.floor(finalTotal * 100) / 100}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }) { values ->
        cartData.data?.data?.let { data ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                if (data.isNotEmpty()) {
                    items(data.size) { index ->
                        var qtyInCart = 1
                        try {
                            val itemInCart =
                                cartItems.first { it.productId == data.get(index).id }
                            qtyInCart = itemInCart.qty
                        } catch (e: Exception) {

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = data.get(index).image1),
                                contentDescription = "Image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth(0.25f)
                                    .aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        data.get(index).name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = {
                                            cartViewModel.deleteItemcart(
                                                data.get(
                                                    index
                                                ).id
                                            )
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = ""
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val error =
                                    if (qtyInCart > data.get(index).availableStock) "Available stock: ${
                                        data.get(index).availableStock
                                    }" else ""
                                Text(
                                    error,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "$${Math.floor((qtyInCart * data.get(index).price) * 100) / 100}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        OutlinedIconButton(
                                            onClick = {
                                                if (qtyInCart > 1) {
                                                    cartViewModel.decreaseItemCart(
                                                        data.get(
                                                            index
                                                        ).id
                                                    )
                                                } else {
                                                    cartViewModel.deleteItemcart(
                                                        data.get(
                                                            index
                                                        ).id
                                                    )
                                                }
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = ""
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = qtyInCart.toString(),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        OutlinedIconButton(
                                            onClick = {
                                                cartViewModel.increaseItemCart(
                                                    data.get(
                                                        index
                                                    ).id
                                                )
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    delivery.data?.data?.let { data ->
                        item {
                            Spacer(modifier = Modifier.height(48.dp))
                            Text(
                                "Shipping method",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(data.size) { index ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                RadioButton(
                                    selected = selectedDelivery?.id == data.get(index).id,
                                    onClick = {
                                        cartViewModel.onChangeSelectedDelivery(data.get(index))
                                    })
                                Column {
                                    Text(
                                        data.get(index).name,
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        "$${data.get(index).price} - ${data.get(index).duration}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Shipping address",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Row() {
                            Column(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = selectedCountry?.name ?: "Country",
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            awaitEachGesture {
                                                awaitFirstDown(pass = PointerEventPass.Initial)
                                                val upEvent =
                                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                                if (upEvent != null) {
                                                    expandedCountry = true
                                                }
                                            }
                                        }, maxLines = 1
                                )
                                DropdownMenu(
                                    expanded = expandedCountry,
                                    onDismissRequest = { expandedCountry = false },
                                ) {
                                    countryState.data?.data?.let { data ->
                                        data.forEach { country ->
                                            DropdownMenuItem(
                                                text = { Text(country.name) },
                                                onClick = {
                                                    expandedCountry = false
                                                    cartViewModel.onChangeCountry(country)
                                                })
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(0.1f))
                            Column(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = selectedState?.name ?: "State",
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .pointerInput(Unit) {
                                            awaitEachGesture {
                                                awaitFirstDown(pass = PointerEventPass.Initial)
                                                val upEvent =
                                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                                if (upEvent != null) {
                                                    expandedState = true
                                                }
                                            }
                                        }, maxLines = 1
                                )
                                DropdownMenu(
                                    expanded = expandedState,
                                    onDismissRequest = { expandedState = false },
                                ) {
                                    selectedCountry?.state?.let { data ->
                                        data.forEach { state ->
                                            DropdownMenuItem(
                                                text = { Text(state.name) },
                                                onClick = {
                                                    expandedState = false
                                                    cartViewModel.onChangeState(state)
                                                })
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row() {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = {
                                    cartViewModel.onChangeFullName(it)
                                },
                                label = {
                                    Text("Fullname")
                                },
                                isError = errorFullName.error,
                                supportingText = {
                                    if (errorFullName.error) {
                                        Text(
                                            errorFullName.message,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f), maxLines = 1
                            )
                            Spacer(modifier = Modifier.weight(0.1f))
                            OutlinedTextField(
                                value = zipCode,
                                onValueChange = {
                                    cartViewModel.onChangeZipCode(it)
                                }, label = {
                                    Text("Zip code")
                                },
                                isError = errorZipCode.error,
                                supportingText = {
                                    if (errorZipCode.error) {
                                        Text(
                                            errorZipCode.message,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f), maxLines = 1,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = {
                                cartViewModel.onChangeAddress(it)
                            },
                            label = {
                                Text("Adress")
                            },
                            isError = errorAddress.error,
                            supportingText = {
                                if (errorAddress.error) {
                                    Text(
                                        errorAddress.message,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Cart is Empty", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}