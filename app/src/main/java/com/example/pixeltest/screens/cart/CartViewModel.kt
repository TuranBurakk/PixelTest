package com.example.pixeltest.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState = _cartState.asStateFlow()



    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            repository.getCart()
                .catch { exception ->
                    _cartState.value = CartState.Error(exception.message)
                }
                .collect { products ->
                    _cartState.value = CartState.Success(products)

                }
        }
    }

    fun increaseQuantity(cartItem: ProductData) {
        viewModelScope.launch {
            val updatedItem = cartItem.copy(quantity = cartItem.quantity + 1)
            repository.updateFromCart(cartItem, updatedItem.quantity)
        }
    }

    fun decreaseQuantity(cartItem: ProductData) {
        viewModelScope.launch {
            if (cartItem.quantity > 1) {
                val updatedItem = cartItem.copy(quantity = cartItem.quantity - 1)
                repository.updateFromCart(updatedItem, updatedItem.quantity)
            } else {
                repository.deleteFromCart(cartItem)
            }
        }
    }


}



sealed class CartState {
    data class Success(val cartItems: List<ProductData>) : CartState()
    data class Error(val exception: String?) : CartState()
    object Loading : CartState()
}
