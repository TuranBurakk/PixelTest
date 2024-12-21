package com.example.pixeltest.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.screens.home.HomeScreenState
import com.example.pixeltest.utlis.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _productState: MutableStateFlow<DetailsScreenState> = MutableStateFlow(
        DetailsScreenState.Loading
    )
    val productState = _productState.asStateFlow()

    private var _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            repository.getProductById(productId).collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        val product = resource.data

                        if (product != null) {
                            checkFavoriteStatus(product.id)
                            _productState.update {
                                DetailsScreenState.Success(product)
                            }
                        }
                    }

                    Resource.Status.ERROR -> {
                        _productState.update {
                            DetailsScreenState.Error(resource.message)
                        }
                    }

                    Resource.Status.LOADING -> {
                        _productState.update {
                            DetailsScreenState.Loading
                        }
                    }
                }
            }
        }
    }

    fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            val favorites = repository.getFavorites().first()
            favorites.forEach { println("Checking favorite: ${it.id} against $productId") } // Tüm karşılaştırmaları yazdır

            val isFavorite = favorites.any { it.id.trim() == productId.trim() }
            println("Favorite check result for $productId: $isFavorite")

            _isFavorite.value = isFavorite
        }
    }


    fun addToFavorites(product: ProductData) {
        viewModelScope.launch {
            println("Adding to favorites: ${product.id}")
            repository.addToFavorites(product)

            // Favorilere eklenen tüm ürünleri kontrol et
            val favorites = repository.getFavorites().first()
            println("Favorites after adding: $favorites") // Favoriler doğru eklenmiş mi?

            // Favori durumunu kontrol et
            checkFavoriteStatus(product.id)
        }
    }


    fun removeFromFavorites(product: ProductData) {
        viewModelScope.launch {
            repository.deleteFromFavorites(product)
            _isFavorite.value = false
            checkFavoriteStatus(product.id)

        }
    }

    fun addToCart(product: ProductData) {
        viewModelScope.launch {
            val savedProduct = repository.getCartById(product.id)

            if (savedProduct != null && savedProduct.isInCart) {
                val newQuantity = savedProduct.quantity + 1
                repository.updateFromCart(savedProduct, quantity = newQuantity)
            } else {
                val newProduct = product.copy(isInCart = true, quantity = 1)
                repository.addToCart(newProduct)
            }

        }
    }
}

sealed class DetailsScreenState {
    data class Success(val product: ProductData) : DetailsScreenState()
    data class Error(val message: String?) : DetailsScreenState()
    object Loading : DetailsScreenState()
}
