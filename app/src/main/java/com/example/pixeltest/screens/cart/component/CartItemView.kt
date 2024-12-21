package com.example.pixeltest.screens.cart.component

import ChromeMinimize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun CartItemView(cartItem: ProductData, navController: NavController, viewModel: CartViewModel , miniusCart:()-> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate("details_screen/${cartItem.id}") }) {
            cartItem.name?.let { Text(it, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            cartItem.price?.let { Text("Price: $it", color = Color.Blue, fontSize = 14.sp) }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .size(40.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    viewModel.decreaseQuantity(cartItem)
                    miniusCart() }) {
                    Icon(
                        ChromeMinimize,
                        contentDescription = "clear",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            IconBox(Color.Blue, cartItem.quantity, "", viewModel, cartItem)

            Spacer(modifier = Modifier.width(2.dp))

            Box(
                modifier = Modifier
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .size(40.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { viewModel.increaseQuantity(cartItem) }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "add",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun IconBox(color: Color, quantity: Int?, iconName: String, viewModel: CartViewModel, productData: ProductData) {
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .size(40.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (quantity == null) {
            IconButton(onClick = { viewModel.increaseQuantity(productData) }) {
                Icon(
                    imageVector = when (iconName) {
                        "add" -> Icons.Outlined.Add
                        "clear" -> Icons.Outlined.Clear
                        else -> Icons.Outlined.Add
                    },
                    contentDescription = iconName,
                    tint = Color.White
                )
            }
        } else {
            Text(
                text = quantity.toString(),
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

