package com.example.pixeltest

import androidx.lifecycle.viewModelScope
import com.example.pixeltest.base.BaseViewModel
import com.example.pixeltest.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            repository.getCart()
                .catch { exception ->
                }
                .collect { cartItems ->
                    val totalQuantity = cartItems.sumOf { it.quantity }
                    _cartQuantity.value = totalQuantity
                }
        }
    }

    fun plusCart() {
        _cartQuantity.update { it + 1 }
    }

    fun miniusCart() {
        _cartQuantity.update { it - 1 }
    }
}




