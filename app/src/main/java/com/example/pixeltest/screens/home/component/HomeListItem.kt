package com.example.pixeltest.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pixeltest.data.entity.ProductData

@Composable
fun HomeListItem(
    productData: ProductData,
    onFavoriteClick: (ProductData) -> Unit,
    navController: NavController,
    addToCart: (ProductData) -> Unit
) {

    val isFavorite = remember { mutableStateOf(productData.isFavorite) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)
            .height(260.dp)
            .shadow(elevation = 1.dp)
            .background(Color.White)
            .clickable {     navController.navigate("details_screen/${productData.id}/${productData.name}") }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(

                    painter = rememberAsyncImagePainter(
                        model = productData.image,
                    ),
                    contentDescription = productData.name,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(10.dp).fillMaxWidth(),

                )

                IconButton(
                    onClick = {
                        isFavorite.value = !isFavorite.value
                        onFavoriteClick(productData)
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
            Column (modifier = Modifier.padding(10.dp)) {

                Text(
                    text = productData.price ?: "",
                    fontSize = 14.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Medium

                )

                Text(
                    text = productData.name ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    fontWeight = FontWeight.Medium
                )
            }
            }

        Button(
            onClick = {addToCart(productData)},
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(40.dp)
            ,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Add to Cart", color = Color.White)
        }


        }

    }

