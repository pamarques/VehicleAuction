package pt.pedromarques.vehicleauction.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.cars.CarListUiState
import pt.pedromarques.vehicleauction.ui.components.Components.PageSelector
import pt.pedromarques.vehicleauction.ui.components.Components.VACarList

@Composable
fun NavigatedCarListScreen(
    title: String,
    uiState: CarListUiState,
    actions: @Composable RowScope.() -> Unit = {},
    onNavigateBack: () -> Unit,
    goToDetails: (Int) -> Unit
) {
    var selectedPage by remember { mutableIntStateOf(0) }
    val resultsPerPage = uiState.paginatedCars?.first()?.size ?: 0
    val totalResults = uiState.filteredCars?.size ?: 0

    NavigatedScreen(
        title = title,
        onNavigateBack = onNavigateBack,
        actions = actions,
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                PageSelector(numberOfPages = uiState.numberOfPages,
                    selectedPage = selectedPage,
                    resultsPerPage = resultsPerPage,
                    totalResults = totalResults,
                    selectPage = {
                        i ->
                    selectedPage = i
                })
            }
    }) { innerPadding ->
        if (uiState.isLoading) {
            Loading(stringResource(R.string.loading_cars))

            return@NavigatedScreen
        }
        VACarList(uiState, innerPadding, selectedPage, goToDetails)
    }
}