package pt.pedromarques.vehicleauction.cars

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pt.pedromarques.vehicleauction.App
import pt.pedromarques.vehicleauction.MainActivity
import pt.pedromarques.vehicleauction.data.CarRepository
import pt.pedromarques.vehicleauction.model.Car

data class CarDetailUiState(
    val isFavourite: Boolean = false,
    val car: Car = Car.sample
)

class CarDetailViewModel(private val repository: CarRepository, private val savedStateHandle: SavedStateHandle): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).repository
                val savedStateHandle = createSavedStateHandle()

                CarDetailViewModel(
                    repository = repository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }

    private val _state = MutableStateFlow(CarDetailUiState())
    val state: StateFlow<CarDetailUiState> = _state.asStateFlow()
    private val carId = savedStateHandle.toRoute<MainActivity.CarId>()

    init {
        reloadCar()
    }

    private fun reloadCar() {
        val car = getCarById(carId.id)
        if (car != null) {
            _state.update {
                it.copy(car  = car, isFavourite = car.favourite)
            }
        }
    }

    private fun getCarById(carId: Int): Car? {
        return repository.getCarById(carId)
    }

    fun addToFavourite() {
        if(_state.value.car.favourite){
            repository.removeFavourite(carId.id)
        } else {
            repository.addFavourite(carId.id)
        }
        reloadCar()
    }
}