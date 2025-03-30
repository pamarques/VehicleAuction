package pt.pedromarques.vehicleauction.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.pedromarques.vehicleauction.App
import pt.pedromarques.vehicleauction.data.CarRepository

class UserFavouriteViewModel(private val repository: CarRepository) : ViewModel(){

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).repository

                UserFavouriteViewModel(repository = repository)
            }
        }
    }

    private val _carListState = MutableStateFlow(CarListUiState())
    val carListState: StateFlow<CarListUiState> = _carListState.asStateFlow()

    fun loadData(){
        viewModelScope.launch {
            _carListState.update { it.copy(isLoading = true) }
            val favourites = repository.getFavourites()
            val paginatedCars = favourites.toList().chunked(10)
            _carListState.update { it.copy(isLoading = false, filteredCars = favourites, paginatedCars = paginatedCars, numberOfPages = favourites.size / 10) }
        }
    }
}