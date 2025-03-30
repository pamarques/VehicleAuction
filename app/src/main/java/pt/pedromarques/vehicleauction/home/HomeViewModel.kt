package pt.pedromarques.vehicleauction.home

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

data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedMake: String = "",
    val selectedModel: String = "",
    val startBidValue: Float = 0f,
    val endBidValue: Float = 0f,
    val makers: List<String> = ArrayList(),
    val models: List<String> = ArrayList()
)

class HomeViewModel(val repository: CarRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as App).repository

                HomeViewModel(
                    repository = repository,
                )
            }
        }
    }

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeSate: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        _homeState.update { currentState -> currentState.copy(isLoading = true) }
        loadData()
    }

    fun loadData() {
        _homeState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            repository.getCars()
            _homeState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    makers = repository.getMakers(),
                    models = repository.getModels(),
                    startBidValue = repository.getMinStartBid(),
                    endBidValue = repository.getMaxStartBid()
                )
            }
        }
    }

    fun updateModelsByMake(make: String) {
        _homeState.update { it ->
            it.copy(models = repository.getModelsByMake(make))
        }
    }

}