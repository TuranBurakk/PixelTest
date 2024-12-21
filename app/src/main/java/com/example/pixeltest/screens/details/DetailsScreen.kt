package com.example.pixeltest.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.screens.details.component.ProductImage
import com.example.pixeltest.screens.details.component.ProductPrice

@Composable
fun DetailsScreen(
    navController: NavController,
    productId: String,
    viewModel: DetailsViewModel = hiltViewModel(),
    plusCart: () ->Unit
) {
    val uiState = viewModel.productState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.getProductDetails(productId)
    }

    when (val state = uiState.value) {
        is DetailsScreenState.Loading -> {
            LoadingScreen()
        }

        is DetailsScreenState.Success -> {
            ProductDetailsContent(navController, state.product, viewModel,plusCart)
        }

        is DetailsScreenState.Error -> {
            ErrorScreen(state.message)
        }

        else -> {}
    }
}


@Composable
fun ProductDetailsContent(
    navController: NavController,
    product: ProductData,
    viewModel: DetailsViewModel,
    plusCart: () -> Unit
) {

        Column(
            modifier = Modifier
                .fillMaxSize()

                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {


                ProductImage(product, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                product.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                product.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ProductPrice(product, viewModel,plusCart)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Error: ${message ?: "Unknown error"}",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}










