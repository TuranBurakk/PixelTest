package com.example.pixeltest.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pixeltest.components.AppTopBar
import com.example.pixeltest.data.entity.ProductData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(
    products: List<ProductData>,
    onFavoriteClick: (ProductData) -> Unit,
    navController: NavController,
    addToCart: (ProductData) -> Unit
) {
    var searchName by remember { mutableStateOf("") }
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    var selectedBrand by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("") }
    var selectedSortCriteria by remember { mutableStateOf("") }

    var filteredProducts by remember { mutableStateOf(products) }

    val applyFilter: () -> Unit = {
        filteredProducts = products.filter {
            (selectedBrand.isEmpty() || it.brand.equals(selectedBrand, ignoreCase = true)) &&
                    (selectedModel.isEmpty() || it.model.equals(selectedModel, ignoreCase = true)) &&
                    it.name!!.contains(searchName, ignoreCase = true)
        }.let { productList ->
            when (selectedSortCriteria) {
                "Old to new" -> productList.sortedBy { it.createdAt }
                "New to old" -> productList.sortedByDescending { it.createdAt }
                "Price high to low" -> productList.sortedByDescending { it.price?.toDoubleOrNull() }
                "Price low to high" -> productList.sortedBy { it.price?.toDoubleOrNull() }
                else -> productList
            }
        }
    }


        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = searchName,
                onValueChange = { searchName = it
                                applyFilter()
                                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Filters:",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )
                Button(
                    onClick = { showFilterBottomSheet = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Select Filter")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredProducts) { product ->
                    HomeListItem(
                        productData = product,
                        onFavoriteClick = onFavoriteClick,
                        navController = navController,
                        addToCart = addToCart
                    )
                }
            }
        }


    if (showFilterBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White,
            onDismissRequest = { showFilterBottomSheet = false }
        ) {
            FilterBottomSheetContent(
                brands = products.mapNotNull { it.brand }.distinct(),
                selectedBrand = selectedBrand,
                onBrandSelected = { selectedBrand = it },
                models = products.mapNotNull { it.model }.distinct(),
                selectedModel = selectedModel,
                onModelSelected = { selectedModel = it },
                sortOptions = listOf("Old to new", "New to old", "Price high to low", "Price low to high"),
                selectedSort = selectedSortCriteria,
                onSortSelected = { selectedSortCriteria = it },
                onApply = {
                    applyFilter()
                    showFilterBottomSheet = false
                },
                onDismiss = { showFilterBottomSheet = false }
            )
        }
    }
}