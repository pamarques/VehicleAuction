package pt.pedromarques.vehicleauction.cars

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.model.Car
import pt.pedromarques.vehicleauction.ui.components.NavigatedCarListScreen

@Composable
fun CarListScreen(viewModel: CarSearchViewModel = viewModel(factory = CarSearchViewModel.Factory),
                  onNavigateBack: () -> Unit,
                  goToDetails: (Int) -> Unit) {
    val uiState by viewModel.carListState.collectAsStateWithLifecycle()

    CarListScreen(uiState = uiState, sortCarsBy = { sortingType ->
        viewModel.sortBy(sortingType)
    }, onNavigateBack = onNavigateBack, goToDetails = goToDetails)
}
@Composable
fun CarListScreen(uiState: CarListUiState,
                  sortCarsBy: (SortingType) -> Unit,
                  onNavigateBack: () -> Unit,
                  goToDetails: (Int) -> Unit){

    var showOptions by remember { mutableStateOf(false) }

    NavigatedCarListScreen(
        title = stringResource(R.string.car_list_title),
        uiState = uiState,
        actions = {
            IconButton( onClick = {
                showOptions = true
            }) {
                Icon(ImageVector.vectorResource(R.drawable.baseline_sort_24), contentDescription = "Localized description")
            }
            DropdownMenu(
                expanded = showOptions,
                onDismissRequest = { showOptions = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Sort by Make Asc") },
                    onClick = {
                        sortCarsBy(SortingType.MakeAsc)
                        showOptions = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sort by Make Desc") },
                    onClick = {
                        sortCarsBy(SortingType.MakeDesc)
                        showOptions = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sort By Starting Bid Asc") },
                    onClick = {
                        sortCarsBy(SortingType.StartingBidAsc)
                        showOptions = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sort By Starting Bid Desc") },
                    onClick = {
                        sortCarsBy(SortingType.StartingBidDesc)
                        showOptions = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sort By Auction Date Asc") },
                    onClick = {
                        sortCarsBy(SortingType.AuctionDateAsc)
                        showOptions = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sort By Auction Date Desc") },
                    onClick = {
                        sortCarsBy(SortingType.AuctionDateDesc)
                        showOptions = false
                    }
                )
            }
        },
        onNavigateBack = onNavigateBack,
        goToDetails = goToDetails)
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_5")
@Composable
fun CarListScreenPreview() {
    val cars = mapOf(
        Pair(0, Car.sample), Pair(1, Car.sample), Pair(2, Car.sample2),
        Pair(3, Car.sample), Pair(4, Car.sample2), Pair(5, Car.sample),
        Pair(6, Car.sample2), Pair(7, Car.sample), Pair(8, Car.sample2),
        Pair(9, Car.sample), Pair(10, Car.sample), Pair(11, Car.sample), Pair(12, Car.sample2),
        Pair(13, Car.sample), Pair(14, Car.sample2), Pair(15, Car.sample),
        Pair(16, Car.sample2), Pair(17, Car.sample), Pair(18, Car.sample2),
        Pair(19, Car.sample))

    MaterialTheme {
        CarListScreen(
            CarListUiState(filteredCars = cars,
                paginatedCars = listOf(cars.toList()),
                numberOfPages = 10),
            sortCarsBy = { _ -> },
            onNavigateBack = {},
            goToDetails = {})
    }
}