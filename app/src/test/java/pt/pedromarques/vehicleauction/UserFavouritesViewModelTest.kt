package pt.pedromarques.vehicleauction

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pt.pedromarques.vehicleauction.cars.UserFavouriteViewModel
import pt.pedromarques.vehicleauction.data.MockCarRepositoryImpl
import pt.pedromarques.vehicleauction.home.HomeViewModel


class UserFavouritesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: UserFavouriteViewModel
    private lateinit var mockRepository: MockCarRepositoryImpl

    // example of unit test mocking a repository, test all viewmodel functions
    @Before
    fun setUp() {
        mockRepository = MockCarRepositoryImpl()
        viewModel = UserFavouriteViewModel(repository = mockRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeViewModel_loadData() = runTest {
        viewModel.loadData()
        advanceUntilIdle()

        val uiState = viewModel.carListState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(mockRepository.getFavourites(), uiState.filteredCars)
    }
}