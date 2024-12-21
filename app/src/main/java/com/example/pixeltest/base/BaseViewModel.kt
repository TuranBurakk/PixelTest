package com.example.pixeltest.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel  : ViewModel() {

    protected val _cartQuantity = MutableStateFlow(0)
    val cartQuantity: StateFlow<Int> = _cartQuantity.asStateFlow()

}


