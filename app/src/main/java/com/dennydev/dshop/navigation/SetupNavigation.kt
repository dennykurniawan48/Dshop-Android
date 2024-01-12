package com.dennydev.dshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dennydev.dshop.screen.CartScreen
import com.dennydev.dshop.screen.DetailOrderScreen
import com.dennydev.dshop.screen.DetailProductScreen
import com.dennydev.dshop.screen.FavoriteScreen
import com.dennydev.dshop.screen.HomeScreen
import com.dennydev.dshop.screen.LoginScreen
import com.dennydev.dshop.screen.ProductCategory
import com.dennydev.dshop.screen.ProfileScreen
import com.dennydev.dshop.screen.PromoScreen
import com.dennydev.dshop.screen.RegisterScreen
import com.dennydev.dshop.screen.SearchScreen
import com.dennydev.dshop.screen.SuccessScreen
import com.dennydev.dshop.viewmodel.MainViewModel

@Composable
fun SetupNavigation(navController: NavHostController, startDestination: String) {
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = startDestination, modifier = Modifier) {
        composable(Screen.LoginScreen.route){
            LoginScreen(navController = navController, loginViewModel = hiltViewModel())
        }

        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController, registerViewModel = hiltViewModel(), loginViewModel = hiltViewModel())
        }

        composable(Screen.PromoScreen.route){
            PromoScreen(navController = navController)
        }

        composable(Screen.HomeScreen.route){
            mainViewModel.onChangeSelectedBottomNav(Screen.HomeScreen.route)
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(Screen.SearchScreen.route){
            mainViewModel.onChangeSelectedBottomNav(Screen.SearchScreen.route)
            SearchScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(Screen.FavoriteScreen.route){
            mainViewModel.onChangeSelectedBottomNav(Screen.FavoriteScreen.route)
            FavoriteScreen(navController = navController, mainViewModel = mainViewModel, favoriteViewModel = hiltViewModel())
        }

        composable(Screen.ProfileScreen.route){
            mainViewModel.onChangeSelectedBottomNav(Screen.ProfileScreen.route)
            ProfileScreen(navController = navController, mainViewModel = mainViewModel, profileViewModel = hiltViewModel())
        }

        composable(Screen.CartScreen.route){ entry ->
            val usePromo = entry.savedStateHandle.get<Boolean>("usePromo") ?: false
            CartScreen(navController = navController, mainViewModel = mainViewModel, cartViewModel = hiltViewModel(), usePromo=usePromo)
        }

        composable(Screen.DetailProductScreen.route, arguments = listOf(
            navArgument(name="slug"){
                type = NavType.StringType
            }
        )){
                it.arguments?.getString("slug")?.let { productId ->
                    DetailProductScreen(navController = navController, detailViewModel = hiltViewModel(), id=productId)
                }
        }

        composable(Screen.DetailOrderScreen.route, arguments = listOf(
            navArgument(name="slug"){
                type = NavType.StringType
            }
        )){
            it.arguments?.getString("slug")?.let { productId ->
                DetailOrderScreen(navController = navController, detailOrderViewModel = hiltViewModel(), id=productId, mainViewModel = mainViewModel)
            }
        }

        composable(Screen.PaymentSuccessScreen.route){ entry ->
            SuccessScreen(navController = navController)
        }

        composable(Screen.ProductCategoryScreen.route, arguments = listOf(
            navArgument(name="id"){
                type = NavType.StringType
            },navArgument(name="name"){
                type = NavType.StringType
            }
        )){
            it.arguments?.getString("id")?.let { productId ->
                it.arguments?.getString("name")?.let { productName ->
                    ProductCategory(
                        navController = navController,
                        viewModel = hiltViewModel(),
                        id = productId,
                        name = productName
                    )
                }
            }
        }
    }
}