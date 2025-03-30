package pt.pedromarques.vehicleauction.cars

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.pedromarques.vehicleauction.App
import pt.pedromarques.vehicleauction.MainActivity
import pt.pedromarques.vehicleauction.data.CarRepository
import pt.pedromarques.vehicleauction.model.Car
import kotlin.math.roundToInt

data class CarListUiState(
    val isLoading: Boolean = false,
    val filteredCars: Map<Int, Car>? = null,
    val paginatedCars: List<List<Pair<Int, Car>>>? = null,
    val numberOfPages: Int = 1
)

open class CarSearchViewModel(
    private val repository: CarRepository,
    private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).repository
                val savedStateHandle = createSavedStateHandle()

                CarSearchViewModel(
                    savedStateHandle = savedStateHandle,
                    repository = repository,
                )
            }
        }
    }

    private val _carListState = MutableStateFlow(CarListUiState())
    val carListState: StateFlow<CarListUiState> = _carListState.asStateFlow()
    internal val carFilters = savedStateHandle.toRoute<MainActivity.CarListFilter>()

    init {
        viewModelScope.launch {
            _carListState.update { it.copy(isLoading =  true) }
            filterList(repository.getCars(), carFilters.make, carFilters.model, carFilters.startBid, carFilters.endBid, carFilters.itemsPerPage, SortingType.None)
        }
    }

    internal fun filterList(carsToFilter: Map<Int, Car>, make: String = "", model: String = "", startBid: Float = 0f, endBid: Float = 0f, itemsPerPage: Int = 10, sortingType: SortingType = SortingType.None) {
        var cars = carsToFilter
        if(endBid > 0){
            cars = cars.filter { m -> m.value.startingBid >= startBid && m.value.startingBid <= endBid }
        }
        if(make.isNotEmpty()){
            cars = cars.filter { m -> m.value.make == make  }
        }
        if(model.isNotEmpty()){
            cars = cars.filter { m -> m.value.model == model  }
        }
        var sortedList: List<Pair<Int, Car>> = emptyList()
        when(sortingType){
            SortingType.None -> {
                sortedList = cars.toList()
            }
            SortingType.MakeAsc -> {
                sortedList = cars.toList().sortedBy { c -> c.second.make }
            }
            SortingType.MakeDesc -> {
                sortedList = cars.toList().sortedByDescending { c -> c.second.make }
            }
            SortingType.StartingBidAsc -> {
                sortedList = cars.toList().sortedBy { c -> c.second.startingBid }
            }
            SortingType.StartingBidDesc -> {
                sortedList = cars.toList().sortedByDescending { c -> c.second.startingBid }
            }
            SortingType.AuctionDateAsc -> {
                sortedList = cars.toList().sortedBy { c -> c.second.auctionDateTime }
            }
            SortingType.AuctionDateDesc -> {
                sortedList = cars.toList().sortedByDescending { c -> c.second.auctionDateTime }
            }
        }
        val paginatedCars = sortedList.toList().chunked(if(itemsPerPage == 0) sortedList.toList().size else itemsPerPage)
        _carListState.update { currentState ->
            currentState.copy(isLoading =  false, filteredCars = cars, paginatedCars = paginatedCars, numberOfPages = ((cars.size + 0.0) / itemsPerPage).roundToInt().coerceAtLeast(1))
        }
    }

    fun sortBy(sortingType: SortingType) {
        carListState.value.filteredCars?.let { filterList(carsToFilter = it, sortingType = sortingType) }
    }
}

enum class SortingType {
                       None, MakeAsc, MakeDesc, StartingBidAsc,
    StartingBidDesc, AuctionDateAsc, AuctionDateDesc
}
