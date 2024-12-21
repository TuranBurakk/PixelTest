package com.example.pixeltest.screens.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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

@Composable
fun FilterBottomSheetContent(
    sortOptions: List<String>,
    selectedSort: String,
    onSortSelected: (String) -> Unit,
    brands: List<String>,
    selectedBrand: String,
    onBrandSelected: (String) -> Unit,
    models: List<String>,
    selectedModel: String,
    onModelSelected: (String) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Filter",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        Column {
            Text("Sort By", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            sortOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(option) }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedSort == option,
                        onClick = { onSortSelected(option) },
                        modifier = Modifier.size(20.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Blue,
                            unselectedColor = Color.Blue,
                            disabledSelectedColor = Color.Gray,
                            disabledUnselectedColor = Color.Gray
                        )
                    )
                    Text(option, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))

                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Column {
            Text("Brand", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            SearchableCheckboxList(
                items = brands,
                selectedItems = listOf(selectedBrand),
                onItemSelected = { onBrandSelected(it) }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Column {
            Text("Model", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            SearchableCheckboxList(
                items = models,
                selectedItems = listOf(selectedModel),
                onItemSelected = { onModelSelected(it) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Apply", color = Color.White, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableCheckboxList(
    items: List<String>,
    selectedItems: List<String>,
    onItemSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.contains(searchQuery, ignoreCase = true) }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )


        LazyColumn(modifier = Modifier.height(120.dp)) {
            items(filteredItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(item) }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedItems.contains(item),
                        onCheckedChange = {
                            if (selectedItems.contains(item)) {
                                onItemSelected("")
                            } else {
                                onItemSelected(item)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Blue,
                            uncheckedColor = Color.Blue,
                            checkmarkColor = Color.White,
                            disabledCheckedColor = Color.Gray,
                            disabledUncheckedColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item, fontSize = 12.sp)
                }
            }
        }
    }
}
