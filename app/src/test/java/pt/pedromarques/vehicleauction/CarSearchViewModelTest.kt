package pt.pedromarques.vehicleauction

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.internalToRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import pt.pedromarques.vehicleauction.cars.CarSearchViewModel
import pt.pedromarques.vehicleauction.cars.SortingType
import pt.pedromarques.vehicleauction.data.MockCarRepositoryImpl

class SavedStateHandleRule(
    private val route: Any,
) : TestWatcher() {
    val savedStateHandleMock: SavedStateHandle = mockk()
    override fun starting(description: Description?) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandleMock.internalToRoute<Any>(any(), any()) } returns route
        super.starting(description)
    }

    override fun finished(description: Description?) {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
        super.finished(description)
    }
}

class CarSearchViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CarSearchViewModel
    private lateinit var mockRepository: MockCarRepositoryImpl

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(MainActivity.CarListFilter(make = "Toyota", model = "", startBid = 0f, endBid = 0f, itemsPerPage = 10))

    // example of unit test mocking a repository and savedState(needed for the filters)
    @Before
    fun setUp() {
        mockRepository = MockCarRepositoryImpl()

        viewModel = CarSearchViewModel(
            repository = mockRepository,
            savedStateHandle = savedStateHandleRule.savedStateHandleMock
        )
    }

    // example of filtering the list by make, several more tests to all types of filter show be made
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeViewModel_filterByMake() = runTest {
        viewModel.filterList(
            make = viewModel.carFilters.make,
            carsToFilter = mockRepository.cars
        )
        advanceUntilIdle()

        val uiState = viewModel.carListState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(1, uiState.filteredCars?.size)
        assertEquals(viewModel.carFilters.make, uiState.filteredCars?.values?.first()?.make)
    }

    // example of test sorting by make, all other sort types should be tested also
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeViewModel_sortByMakeAsc() = runTest {
        viewModel.filterList(
            make = "",
            carsToFilter = mockRepository.cars
        )
        viewModel.sortBy(SortingType.MakeAsc)
        advanceUntilIdle()

        val uiState = viewModel.carListState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(3, uiState.filteredCars?.size)
        assertEquals("Ford", uiState.paginatedCars?.get(0)?.first()?.second?.make)
    }
}