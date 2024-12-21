package com.example.pixeltest

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pixeltest.components.AppBottomBar
import com.example.pixeltest.components.AppTopBar
import com.example.pixeltest.screens.cart.CartScreen
import com.example.pixeltest.screens.details.DetailsScreen
import com.example.pixeltest.ui.theme.PixelTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixelTestTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()
                val cartQuantity by mainViewModel.cartQuantity.collectAsState()


                Scaffold(
                    topBar = {
                        val currentBackStackEntry = navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStackEntry.value?.destination?.route

                        if (currentRoute?.startsWith("details_screen") == true) {
                            val productName = currentBackStackEntry.value?.arguments?.getString("productName") ?: "Details"
                            AppTopBar(navController = navController, title = productName,true)
                        } else {
                            AppTopBar(navController = navController)
                        }
                    },
                    bottomBar = {
                        AppBottomBar(navController = navController, count = cartQuantity)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController, plusCart = { mainViewModel.plusCart() })
                        }
                        composable(
                            "details_screen/{productId}/{productName}",
                            arguments = listOf(
                                navArgument("productId") { type = NavType.StringType },
                                navArgument("productName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")
                            val productName = backStackEntry.arguments?.getString("productName")
                            productId?.let {
                                DetailsScreen(navController = navController, productId = it, plusCart = { mainViewModel.plusCart() })
                            }
                        }
                        composable("cart") {
                            CartScreen(navController = navController, miniusCart = { mainViewModel.miniusCart() })
                        }
                        composable("profile") {
                            ProfileScreen(navController = navController)
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun ProfileScreen(navController: NavController) {
    Text(
        text = "Welcome to Profile Screen!",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PixelTestTheme {

    }
}
