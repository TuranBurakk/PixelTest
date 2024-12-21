package com.example.pixeltest.components

import Shopping_basket
import Star
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun AppBottomBar(navController: NavController,count:Int) {


    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarIconWithBadge(
                icon = Icons.Outlined.Home,
                label = "Home",
                onClick = { navController.navigate("home") }
            )
            BottomBarIconWithBadge(
                icon = Shopping_basket,
                label = "Cart",
                badgeCount = count,
                onClick = { navController.navigate("cart") }
            )
            BottomBarIconWithBadge(
                icon = Star,
                label = "Search",
                onClick = {}
            )


            BottomBarIconWithBadge(
                icon = Icons.Outlined.Person,
                label = "Profile",
                onClick = { navController.navigate("profile") }
            )
        }
    }
}
@Composable
fun BottomBarIconWithBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box(modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Black,
                modifier = Modifier.align(Alignment.Center).size(26.dp)
            )

            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Badge(
                        containerColor = Color.Red
                    ) {
                        Text(
                            text = "$badgeCount",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

