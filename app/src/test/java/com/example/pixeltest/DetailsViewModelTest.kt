package com.example.pixeltest

import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.screens.details.DetailsScreenState
import com.example.pixeltest.screens.details.DetailsViewModel
import com.example.pixeltest.utlis.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel(repository)
    }

    @Test
    fun `getProductDetails should update UI state with success when product is found`() = runTest {
        // Prepare mock data
        val product = ProductData(
            id = "1",
            brand = "Lamborghini",
            model = "CTS",
            description = "Quasi adipisci sint veniam delectus. Illum laboriosam minima dignissimos natus earum facere consequuntur eius vero. Itaque facilis at tempore ipsa. Accusamus nihil fugit velit possimus expedita error porro aliquid. Optio magni mollitia veritatis repudiandae tenetur nemo. Id consectetur fuga ipsam quidem voluptatibus sed magni dolore.\nFacilis commodi dolores sapiente delectus nihil ex a perferendis. Totam deserunt assumenda inventore. Incidunt nesciunt adipisci natus porro deleniti nisi incidunt laudantium soluta. Nostrum optio ab facilis quisquam.\nSoluta laudantium ipsa ut accusantium possimus rem. Illo voluptatibus culpa incidunt repudiandae placeat animi. Delectus id in animi incidunt autem. Ipsum provident beatae nisi cumque nulla iure.",
            price = "51.00",
            image = "https://loremflickr.com/640/480/food",
            name = "Bentley Focus",
            createdAt = "2023-07-17T07:21:02.529Z",
            isFavorite = false,
            isInCart = false,
            quantity = 1
        )

        // Mock repository call to return success
        coEvery { repository.getProductById(product.id) } returns flow { emit(Resource.success(product)) }

        // Call the method being tested
        viewModel.getProductDetails(product.id)

        // Wait for the UI state to be updated
        advanceUntilIdle()

        // Validate the state of the viewModel
        assertTrue(viewModel.productState.value is DetailsScreenState.Success)

        // Validate the content of the state
        val successState = viewModel.productState.value as DetailsScreenState.Success
        assertEquals(product, successState.product)
    }

    @Test
    fun `getProductDetails should update UI state with error when product is not found`() = runTest {
        coEvery { repository.getProductById("1") } returns flow { emit(Resource.error("Product not found", null)) }

        viewModel.getProductDetails("1")

        assertTrue(viewModel.productState.value is DetailsScreenState.Error)
        val errorState = viewModel.productState.value as DetailsScreenState.Error
        assertEquals("Product not found", errorState.message)
    }

    @Test
    fun `checkFavoriteStatus should update isFavorite to true when product is in favorites`() = runTest {
        val productId = "1"
        val favorites = listOf(ProductData(id = productId, name = "Product 1", price = 100.0.toString(), isFavorite = false))
        coEvery { repository.getFavorites() } returns flow { emit(favorites) }

        viewModel.checkFavoriteStatus(productId)

        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `checkFavoriteStatus should update isFavorite to false when product is not in favorites`() = runTest {
        val productId = "1"
        val favorites = emptyList<ProductData>()
        coEvery { repository.getFavorites() } returns flow { emit(favorites) }

        viewModel.checkFavoriteStatus(productId)

        assertFalse(viewModel.isFavorite.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

