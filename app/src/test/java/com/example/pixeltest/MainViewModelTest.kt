package com.example.pixeltest

import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import io.mockk.coEvery
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
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: Repository
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)

        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)

        viewModel = MainViewModel(repository)

        val cartItems = listOf(
            ProductData(
                id = "1",
                brand = "Lamborghini",
                model = "CTS",
                description = "Quasi adipisci sint veniam delectus.",
                price = "51.00",
                image = "https://loremflickr.com/640/480/food",
                name = "Bentley Focus",
                createdAt = "2023-07-17T07:21:02.529Z",
                isFavorite = false,
                isInCart = false,
                quantity = 1
            ),
            ProductData(
                id = "2",
                brand = "Lamborghini",
                model = "CTS",
                description = "Quasi adipisci sint veniam delectus.",
                price = "51.00",
                image = "https://loremflickr.com/640/480/food",
                name = "Bentley Focus",
                createdAt = "2023-07-17T07:21:02.529Z",
                isFavorite = false,
                isInCart = false,
                quantity = 3
            )
        )

        coEvery { repository.getCart() } returns flow { emit(cartItems) }
    }

    @Test
    fun `fetchCartItems should update cart quantity when cart items are fetched`() = runTest {
        viewModel.fetchCartItems()

        val expectedQuantity = 1 + 3 // 1 + 3 = 4
        assertEquals(expectedQuantity, viewModel.cartQuantity.value)
    }

    @Test
    fun `plusCart should increment cart quantity by 1`() = runTest {
        viewModel.fetchCartItems()

        viewModel.plusCart()

        assertEquals(5, viewModel.cartQuantity.value)
    }

    @Test
    fun `miniusCart should decrement cart quantity by 1`() = runTest {
        viewModel.fetchCartItems()
        viewModel.plusCart()

        viewModel.miniusCart()

        assertEquals(4, viewModel.cartQuantity.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
