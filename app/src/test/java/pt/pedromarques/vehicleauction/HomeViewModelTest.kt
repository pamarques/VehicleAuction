package pt.pedromarques.vehicleauction

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pt.pedromarques.vehicleauction.data.MockCarRepositoryImpl
import pt.pedromarques.vehicleauction.home.HomeViewModel


class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var mockRepository: MockCarRepositoryImpl

    // example of unit test mocking a repository, test all viewmodel functions
    @Before
    fun setUp() {
        mockRepository = MockCarRepositoryImpl()
        viewModel = HomeViewModel(repository = mockRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeViewModel_loadData() = runTest {
        viewModel.loadData()
        advanceUntilIdle()

        val uiState = viewModel.homeSate.value
        assertEquals(false, uiState.isLoading)
        assertEquals(mockRepository.getModels(), uiState.models)
        assertEquals(mockRepository.getMakers(), uiState.makers)
        assertEquals(mockRepository.getMinStartBid(), uiState.startBidValue)
        assertEquals(mockRepository.getMaxStartBid(), uiState.endBidValue)
        assertEquals("", uiState.selectedMake)
        assertEquals("", uiState.selectedModel)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun homeViewModel_updateModelsByMake() = runTest {
        val make = "Toyota"
        viewModel.updateModelsByMake(make = make)
        advanceUntilIdle()

        val uiState = viewModel.homeSate.value
        assertEquals(false, uiState.isLoading)
        assertEquals(mockRepository.getModelsByMake(make), uiState.models)
    }
}