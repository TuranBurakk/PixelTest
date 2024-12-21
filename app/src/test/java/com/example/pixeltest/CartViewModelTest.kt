package com.example.pixeltest

import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.screens.cart.CartState
import com.example.pixeltest.screens.cart.CartViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var repository: Repository
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        // Mocking Repository
        repository = mockk(relaxed = true)

        // Test Dispatcher
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel(repository)

        val cartItems = listOf(
            ProductData(
                id = "1",
                brand = "Brand A",
                model = "Model A",
                description = "Description A",
                price = "10.00",
                image = "https://image.url",
                name = "Product A",
                createdAt = "2023-01-01",
                isFavorite = false,
                isInCart = true,
                quantity = 2
            ),
            ProductData(
                id = "2",
                brand = "Brand B",
                model = "Model B",
                description = "Description B",
                price = "15.00",
                image = "https://image.url",
                name = "Product B",
                createdAt = "2023-01-01",
                isFavorite = false,
                isInCart = true,
                quantity = 1
            )
        )

        // then
        coEvery { repository.getCart() } returns flow { emit(cartItems) }
    }

    @Test
    fun `fetchCartItems should update cartState with Success when items are fetched`() = runTest {
        // given
        viewModel.fetchCartItems()
        val successState = CartState.Success(
            listOf(
                ProductData(
                    id = "1",
                    brand = "Brand A",
                    model = "Model A",
                    description = "Description A",
                    price = "10.00",
                    image = "https://image.url",
                    name = "Product A",
                    createdAt = "2023-01-01",
                    isFavorite = false,
                    isInCart = true,
                    quantity = 2
                ),
                ProductData(
                    id = "2",
                    brand = "Brand B",
                    model = "Model B",
                    description = "Description B",
                    price = "15.00",
                    image = "https://image.url",
                    name = "Product B",
                    createdAt = "2023-01-01",
                    isFavorite = false,
                    isInCart = true,
                    quantity = 1
                )
            )
        )
            //then
        assertEquals(successState, viewModel.cartState.value)
    }

    @Test
    fun `increaseQuantity should call updateFromCart when quantity is increased`() = runTest {
        // given
        val cartItem = ProductData(
            id = "1", brand = "Brand A", model = "Model A", description = "Description A",
            price = "10.00", image = "https://image.url", name = "Product A", createdAt = "2023-01-01",
            isFavorite = false, isInCart = true, quantity = 2
        )

        // when
        viewModel.increaseQuantity(cartItem)

        // then
        coVerify { repository.updateFromCart(cartItem, 3) }
    }

    @Test
    fun `decreaseQuantity should call updateFromCart when quantity is decreased`() = runTest {
        // given
        val cartItem = ProductData(
            id = "1", brand = "Brand A", model = "Model A", description = "Description A",
            price = "10.00", image = "https://image.url", name = "Product A", createdAt = "2023-01-01",
            isFavorite = false, isInCart = true, quantity = 2
        )

        // when
        viewModel.decreaseQuantity(cartItem)

        // then
        coVerify { repository.updateFromCart(cartItem.copy(quantity = 1), 1) }
    }


    @Test
    fun `decreaseQuantity should call deleteFromCart when quantity is 1`() = runTest {
        // given
        val cartItem = ProductData(
            id = "2", brand = "Brand B", model = "Model B", description = "Description B",
            price = "15.00", image = "https://image.url", name = "Product B", createdAt = "2023-01-01",
            isFavorite = false, isInCart = true, quantity = 1
        )

        // when
        viewModel.decreaseQuantity(cartItem)

        // then
        coVerify { repository.deleteFromCart(cartItem) }
    }

    @After
    fun tearDown() {
        // Clean up test dispatcher after test
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
