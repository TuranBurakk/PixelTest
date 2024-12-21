package com.example.pixeltest.screens.details.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.screens.details.DetailsViewModel

@Composable
fun ProductImage(product: ProductData, viewModel: DetailsViewModel) {
    val isFavorite = viewModel.isFavorite.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(
                model = product.image
            ),
            contentDescription = "Product Image",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(250.dp)
                .aspectRatio(1f)
        )

        IconButton(
            onClick = {
                if (isFavorite.value) {
                    viewModel.removeFromFavorites(product)
                } else {
                    viewModel.addToFavorites(product)
                }
            },
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Add to Favorites",
                tint = if (isFavorite.value) Color.Yellow else Color.Gray
            )
        }
    }
}
