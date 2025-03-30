package pt.pedromarques.vehicleauction.cars

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.model.Car
import pt.pedromarques.vehicleauction.ui.components.NavigatedCarListScreen

@Composable
fun FavouritesScreen(viewModel: UserFavouriteViewModel = viewModel(factory = UserFavouriteViewModel.Factory),
                     onNavigateBack: () -> Unit,
                     goToDetails: (Int) -> Unit){
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val uiState by viewModel.carListState.collectAsStateWithLifecycle()

    FavouritesScreen(uiState = uiState, onNavigateBack = onNavigateBack, goToDetails = goToDetails)
}

@Composable
fun FavouritesScreen(
    uiState: CarListUiState,
    onNavigateBack: () -> Unit,
    goToDetails: (Int) -> Unit
) {

    NavigatedCarListScreen(
        title = stringResource(R.string.favourites_list_title),
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        goToDetails = goToDetails)
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_5")
@Composable
fun UserFavouritesScreenPreview() {
    val cars = mapOf(
        Pair(0, Car.sample), Pair(1, Car.sample), Pair(2, Car.sample2),
        Pair(3, Car.sample), Pair(4, Car.sample2), Pair(5, Car.sample),
        Pair(6, Car.sample2), Pair(7, Car.sample), Pair(8, Car.sample2),
        Pair(9, Car.sample), Pair(10, Car.sample), Pair(11, Car.sample), Pair(12, Car.sample2),
        Pair(13, Car.sample), Pair(14, Car.sample2), Pair(15, Car.sample),
        Pair(16, Car.sample2), Pair(17, Car.sample), Pair(18, Car.sample2),
        Pair(19, Car.sample))

    MaterialTheme {
        FavouritesScreen(
            CarListUiState(filteredCars = cars,
                paginatedCars = listOf(cars.toList()),
                numberOfPages = 10),
            onNavigateBack = {},
            goToDetails = {})
    }
}
