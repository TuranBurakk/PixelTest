package com.example.pixeltest.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.pixeltest.screens.cart.component.CartListView

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel(),
    miniusCart: () -> Unit
) {
    val uiState = viewModel.cartState.collectAsStateWithLifecycle()



    when (val state = uiState.value) {
        CartState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)

            )
        }

        is CartState.Success -> {
            val totalPrice =
                state.cartItems.sumOf { (it.price?.toDouble() ?: 0.0) * (it.quantity ?: 1) }
            val cartItems = state.cartItems
            if (cartItems.isEmpty()) {
                EmptyCartView()
            } else {
                CartListView(
                    cartItems,
                    viewModel,

                    navController,
                    totalPrice,
                    miniusCart
                )
            }
        }

        is CartState.Error -> {
            ErrorScreen(state.exception)
        }
    }
}


@Composable
fun EmptyCartView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your cart is empty!",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorScreen(exception: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = exception ?: "An unexpected error occurred.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}