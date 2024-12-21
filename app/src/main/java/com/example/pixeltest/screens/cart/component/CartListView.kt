package com.example.pixeltest.screens.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.screens.cart.CartViewModel

@Composable
fun CartListView(
    products: List<ProductData>,
    viewModel: CartViewModel,

    navController: NavController,
    totalPrice: Double,
    miniusCart: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { cartItem ->
                CartItemView(cartItem, navController, viewModel, miniusCart)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Total:",
                        fontSize = 14.sp,
                        color = Color.Blue,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$totalPrice",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 2.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .height(40.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("Complete", color = Color.White)
                }
            }
        }
    }
}