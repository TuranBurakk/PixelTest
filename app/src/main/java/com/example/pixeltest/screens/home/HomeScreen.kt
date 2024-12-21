import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.pixeltest.screens.home.HomeScreenState
import com.example.pixeltest.screens.home.HomeViewModel
import com.example.pixeltest.screens.home.component.ProductList

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel() ,  plusCart: () ->Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        HomeScreenState.Loading -> {
            LoadingScreen()
        }
        is HomeScreenState.Success -> {
            state.success?.let { products ->
                ProductList(
                    products = products,
                    onFavoriteClick = { product ->
                        val updatedProduct = product.copy(isFavorite = !product.isFavorite)
                        if (updatedProduct.isFavorite) {
                            viewModel.addToFavorites(updatedProduct)
                        } else {
                            viewModel.deleteFromFavorites(updatedProduct)
                        }
                    },
                    navController,
                    addToCart = { product ->
                        if(!product.isInCart){
                            viewModel.addToCart(product)
                            plusCart()
                        }
                    }
                )
            }
        }
        is HomeScreenState.Error -> {
            ErrorScreen(state.exception)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: "An error occurred",
            color = Color.Red,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}










