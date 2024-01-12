package com.dennydev.dshop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val unselectedIcon: ImageVector, val selectedIcon: ImageVector, val route: String, val title: String) {
    object LoginScreen: Screen(Icons.Default.Check, Icons.Default.Check, "Login", "Login")
    object RegisterScreen: Screen(Icons.Default.Check, Icons.Default.Check, "Register", "Register")
    object HomeScreen: Screen(Icons.Outlined.Home, Icons.Filled.Home, "Home", "Home")
    object SearchScreen: Screen(Icons.Outlined.Search, Icons.Filled.Search, "Search", "Search")
    object CartScreen: Screen(Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag, "Cart", "Cart")
    object FavoriteScreen: Screen(Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite, "Favorite", "Favorite")
    object ProfileScreen: Screen(Icons.Outlined.Person, Icons.Filled.Person, "Profile", "Profile")
    object PromoScreen: Screen(Icons.Default.Check, Icons.Default.Check, "Promo", "Promo")
    object PaymentSuccessScreen: Screen(Icons.Default.Check, Icons.Default.Check, "Success", "Success")
    object DetailProductScreen: Screen(Icons.Default.Check, Icons.Default.Check, "DetailProduct/{slug}", "Detail Product")
    object DetailOrderScreen: Screen(Icons.Default.Check, Icons.Default.Check, "DetailOrder/{slug}", "Detail Order")
    object ProductCategoryScreen: Screen(Icons.Default.Check, Icons.Default.Check, "ProductCategory/{id}/{name}", "Product Category")
}