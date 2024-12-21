package com.example.pixeltest

import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.screens.details.DetailsScreenState
import com.example.pixeltest.screens.details.DetailsViewModel
import com.example.pixeltest.utlis.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var repository: Repository
    private lateinit var testDispatcher: TestCoroutineDispatcher

    private val product = ProductData(
        id = "1",
        brand = "Lamborghini",
        model = "CTS",
        description = "Quasi adipisci sint veniam delectus...",
        price = "51.00",
        image = "https://loremflickr.com/640/480/food",
        name = "Bentley Focus",
        createdAt = "2023-07-17T07:21:02.529Z",
        isFavorite = false,
        isInCart = false,
        quantity = 1
    )

    @Before
    fun setUp() {
        // Mock repository
        repository = mockk(relaxed = true)
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel(repository)

        // Mock repository call to return success with the product
        coEvery { repository.getProductById(product.id) } returns flow { emit(Resource.success(product)) }

        // Set up any other necessary mocks, like favorites or other repository calls
    }



    @Test
    fun `getProductDetails should update UI state with error when product is not found`() = runTest {
        // Mock repository call to return error
        coEvery { repository.getProductById("1") } returns flow { emit(Resource.error("Product not found", null)) }

        // Call the method being tested
        viewModel.getProductDetails("1")

        // Wait for state update (using delay or advanceUntilIdle())
        advanceUntilIdle()

        val currentState = viewModel.productState.value
        assertTrue(currentState is DetailsScreenState.Error)
        val errorState = currentState as DetailsScreenState.Error
        assertEquals("Product not found", errorState.message)
    }

    @Test
    fun `checkFavoriteStatus should update isFavorite to true when product is in favorites`() = runTest {
        val productId = "1"
        val favorites = listOf(ProductData(id = productId, name = "Product 1", price = "100.0", isFavorite = true))
        coEvery { repository.getFavorites() } returns flow { emit(favorites) }

        // Call the method being tested
        viewModel.checkFavoriteStatus(productId)

        // Wait for the UI state to be updated
        advanceUntilIdle()

        // Assert that the isFavorite state is updated to true
        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `checkFavoriteStatus should update isFavorite to false when product is not in favorites`() = runTest {
        val productId = "1"
        val favorites = emptyList<ProductData>()
        coEvery { repository.getFavorites() } returns flow { emit(favorites) }

        // Call the method being tested
        viewModel.checkFavoriteStatus(productId)

        // Wait for the UI state to be updated
        advanceUntilIdle()

        // Assert that the isFavorite state is updated to false
        assertFalse(viewModel.isFavorite.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
