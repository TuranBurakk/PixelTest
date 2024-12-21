package com.example.pixeltest.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.utlis.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _uiState: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _favorites: MutableStateFlow<List<ProductData>> = MutableStateFlow(emptyList())

    init {
        loadProducts()
        getFavorites()
    }

    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts().collect { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        val updatedProducts = resource.data?.map { product ->
                            val isFavorite = _favorites.value.any { it.id == product.id }
                            product.copy(isFavorite = isFavorite)
                        }
                        _uiState.update { HomeScreenState.Success(updatedProducts) }
                    }
                    Resource.Status.ERROR -> {
                        _uiState.update { HomeScreenState.Error(resource.message) }
                    }
                    Resource.Status.LOADING -> {
                        _uiState.update { HomeScreenState.Loading }
                    }
                }
            }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { favoriteProducts ->
                _favorites.value = favoriteProducts
            }
        }
    }

    fun addToFavorites(product: ProductData) {
        viewModelScope.launch {
            repository.addToFavorites(product)
            updateProductFavoriteStatus(product, true)
        }
    }

    fun deleteFromFavorites(product: ProductData) {
        viewModelScope.launch {
            repository.deleteFromFavorites(product)
            updateProductFavoriteStatus(product, false)
        }
    }

    private fun updateProductFavoriteStatus(product: ProductData, isFavorite: Boolean) {
        _favorites.value = _favorites.value.map {
            if (it.id == product.id) it.copy(isFavorite = isFavorite) else it
        }
        val updatedProducts = (_uiState.value as? HomeScreenState.Success)?.success?.map { p ->
            if (p.id == product.id) p.copy(isFavorite = isFavorite) else p
        }
        _uiState.update {
            HomeScreenState.Success(updatedProducts)
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


sealed class HomeScreenState(){
    data class Success(val success: List<ProductData>?) : HomeScreenState()
    data class Error(val exception: String?) : HomeScreenState()
    data object Loading : HomeScreenState()
}