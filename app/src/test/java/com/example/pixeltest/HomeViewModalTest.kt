import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.repository.Repository
import com.example.pixeltest.screens.home.HomeScreenState
import com.example.pixeltest.screens.home.HomeViewModel
import com.example.pixeltest.utlis.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val repository: Repository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `loadProducts success updates uiState with products`() = runTest {
        //given
        val products = listOf(
            ProductData(
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
            ),
            ProductData(
                id = "2",
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
        )
        val resource = Resource.success(products)
        coEvery { repository.getProducts() } returns flowOf(resource)
        //when
        viewModel.loadProducts()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is HomeScreenState.Success)
        //then
        assertEquals(products, (uiState as HomeScreenState.Success).success)
    }

    @Test
    fun `loadProducts error updates uiState with error`() = runTest {
        //given
        val errorMessage = "Network error"
        val resource = Resource.error<List<ProductData>>(errorMessage, null)
        coEvery { repository.getProducts() } returns flowOf(resource)
        //when
        viewModel.loadProducts()
        val uiState = viewModel.uiState.value
        assertTrue(uiState is HomeScreenState.Error)
        //then
        assertEquals(errorMessage, (uiState as HomeScreenState.Error).exception)
    }

    @Test
    fun `addToFavorites updates product as favorite`() = runTest {
        //given
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
        coEvery { repository.addToFavorites(product) } just Runs
        //when
        viewModel.addToFavorites(product)
        //then
        coVerify { repository.addToFavorites(product) }
    }

    @Test
    fun `addToCart adds product to cart with correct quantity`() = runTest {
        //given
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
        coEvery { repository.getCartById(product.id) } returns null
        coEvery { repository.addToCart(any()) } just Runs
        //when
        viewModel.addToCart(product)
        //then
        coVerify { repository.addToCart(match { it.id == product.id && it.isInCart && it.quantity == 1 }) }
    }

    @Test
    fun `deleteFromFavorites removes product from favorites`() = runTest {
        //given
        val product = ProductData(
            id = "1",
            brand = "Lamborghini",
            model = "CTS",
            description = "Quasi adipisci sint veniam delectus. Illum laboriosam minima dignissimos natus earum facere consequuntur eius vero. Itaque facilis at tempore ipsa. Accusamus nihil fugit velit possimus expedita error porro aliquid. Optio magni mollitia veritatis repudiandae tenetur nemo. Id consectetur fuga ipsam quidem voluptatibus sed magni dolore.\nFacilis commodi dolores sapiente delectus nihil ex a perferendis. Totam deserunt assumenda inventore. Incidunt nesciunt adipisci natus porro deleniti nisi incidunt laudantium soluta. Nostrum optio ab facilis quisquam.\nSoluta laudantium ipsa ut accusantium possimus rem. Illo voluptatibus culpa incidunt repudiandae placeat animi. Delectus id in animi incidunt autem. Ipsum provident beatae nisi cumque nulla iure.",
            price = "51.00",
            image = "https://loremflickr.com/640/480/food",
            name = "Bentley Focus",
            createdAt = "2023-07-17T07:21:02.529Z",
            isFavorite = true,
            isInCart = false,
            quantity = 1
        )
        coEvery { repository.deleteFromFavorites(product) } just Runs
        //when
        viewModel.deleteFromFavorites(product)
        //then
        coVerify { repository.deleteFromFavorites(product) }
    }
}
