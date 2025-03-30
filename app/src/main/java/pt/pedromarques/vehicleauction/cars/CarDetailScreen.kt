package pt.pedromarques.vehicleauction.cars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.model.Car
import pt.pedromarques.vehicleauction.ui.components.NavigatedScreen
import pt.pedromarques.vehicleauction.ui.components.VABigTitleText
import pt.pedromarques.vehicleauction.ui.components.VAIconText
import pt.pedromarques.vehicleauction.ui.components.VAText
import pt.pedromarques.vehicleauction.ui.components.VATitleText


@Composable
fun CarDetailScreen(viewModel: CarDetailViewModel = viewModel(factory = CarDetailViewModel.Factory),
                    onNavigateBack: () -> Unit,){

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    CarDetailScreen(state = uiState,
    onNavigateBack = onNavigateBack,
        addToFavourite = { viewModel.addToFavourite() })
}

@Composable
fun CarDetailScreen(state: CarDetailUiState, onNavigateBack: () -> Unit, addToFavourite: () -> Unit){
    val imageHeight = 250.dp
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackMessage = if(state.isFavourite) stringResource(R.string.remove_favourite) else stringResource(R.string.add_favourite)
    val car = state.car
    val scrollState = rememberScrollState()

    NavigatedScreen(title = car.shortTitle(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        onNavigateBack = onNavigateBack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        addToFavourite()
                        snackbarHostState.showSnackbar(
                            message = snackMessage,
                            duration = SnackbarDuration.Short)
                    }
                },
            ) {
                Icon(if(state.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, "add to Favourite.")
            }
        }) { innerPadding ->
        Column(Modifier.verticalScroll(scrollState)
            .padding(innerPadding)
            .fillMaxWidth()) {
            Box{
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .background(Color.LightGray)){
                    Image(bitmap = ImageBitmap.imageResource(R.drawable.placeholder),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "Car Image")
//                    Icon(if(state.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
//                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
//                        tint = Color.Red,
//                        contentDescription = "Favourite")
                }
                Column {
                    Spacer(Modifier.height(imageHeight - 16.dp))
                    Column(Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(MaterialTheme.colorScheme.background)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            VABigTitleText(modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 16.dp),
                                text = car.shortTitle(),
                                textAlign = TextAlign.Start)
                            VATitleText(modifier = Modifier.padding(horizontal = 16.dp),
                                text = String.format("%s €", car.startingBid),
                                textAlign = TextAlign.End)
                        }
                        LazyRow(
                            Modifier
                                .fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {
                            item {
                                CarDetailMain(icon = ImageVector.vectorResource(R.drawable.baseline_access_time_24),
                                    text = car.year.toString(),
                                    label = stringResource(R.string.year))
                            }
                            item {
                                CarDetailMain(icon = ImageVector.vectorResource(R.drawable.baseline_speed_24),
                                    text = car.mileage.toString(),
                                    label = stringResource(R.string.miles))
                            }
                            item {
                                CarDetailMain(icon = ImageVector.vectorResource(R.drawable.baseline_electric_bolt_24),
                                    text = car.fuel,
                                    label = stringResource(R.string.fuel_type))
                            }
                            item {
                                CarDetailMain(icon = ImageVector.vectorResource(R.drawable.baseline_directions_car_24),
                                    text = car.engineSize,
                                    label = stringResource(R.string.engine_size))
                            }
                        }
                        if(car.details != null){
                            VABigTitleText(modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 16.dp),
                                text = "Details",
                                textAlign = TextAlign.Start)
                            Column {
                                CarDetailItem(label = stringResource(R.string.vehicle_type),
                                    text = car.details.specification.vehicleType)
                                CarDetailItem(label = stringResource(R.string.colour),
                                    text = car.details.specification.colour)
                                CarDetailItem(label = stringResource(R.string.transmission),
                                    text = car.details.specification.transmission)
                                CarDetailItem(label = stringResource(R.string.number_of_doors),
                                    text = car.details.specification.numberOfDoors.toString())
                                CarDetailItem(label = stringResource(R.string.co2Emissions),
                                    text = car.details.specification.co2Emissions)
                                CarDetailItem(label = stringResource(R.string.noxEmissions),
                                    text = car.details.specification.noxEmissions.toString())
                                CarDetailItem(label = stringResource(R.string.numberOfKeys),
                                    text = car.details.specification.numberOfKeys.toString())
                            }

                            VABigTitleText(modifier = Modifier.padding(16.dp),
                                text = "Equipment",
                                textAlign = TextAlign.Start)
                            car.details.equipment.forEach{ equip ->
                                VAText(text = equip,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
                            }

                        }
                        Spacer(Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CarDetailItem(label: String, text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        VATitleText(modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 16.dp),
            text = label,
            textAlign = TextAlign.Start)
        VAText(modifier = Modifier.padding(horizontal = 16.dp),
            text = text,
            textAlign = TextAlign.End)

    }
    HorizontalDivider()
}

@Composable
fun CarDetailMain(icon: ImageVector, text: String, label: String) {
    Column(Modifier.width(120.dp).padding(horizontal = 8.dp)
        .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(8.dp))
        .padding(horizontal = 8.dp, vertical = 16.dp)
    ){
        Icon(icon,
            modifier = Modifier.padding(vertical = 4.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = "Favourite")
        VABigTitleText(modifier = Modifier,
            text = text,
            textAlign = TextAlign.Start)
        VAText(modifier = Modifier,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
            text = label,
            textAlign = TextAlign.Start)
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailScreenPreview() {
    CarDetailScreen(CarDetailUiState(car = Car.sample),
        onNavigateBack = {}, addToFavourite = {})
}