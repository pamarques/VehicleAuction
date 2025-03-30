package pt.pedromarques.vehicleauction.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.cars.CarListUiState
import pt.pedromarques.vehicleauction.model.Car
import pt.pedromarques.vehicleauction.ui.components.Components.CarListItem
import pt.pedromarques.vehicleauction.ui.components.Components.PageSelector
import pt.pedromarques.vehicleauction.ui.components.Components.VACarList
import pt.pedromarques.vehicleauction.ui.components.Components.VARangeSlider
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Components {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun VARangeSlider(modifier: Modifier,
                      minValue: Float, maxValue: Float,
                      label: String,
                      onNewRangeSelected: (Float, Float) -> Unit){

        val rangeSliderState = remember {
            RangeSliderState(
                minValue,
                maxValue,
                steps = (maxValue / 100 - 1).toInt().coerceAtLeast(0),
                valueRange = minValue..maxValue
            )
        }
        VARangeSlider(modifier = modifier, label = label, rangeSliderState = rangeSliderState, onNewRangeSelected = onNewRangeSelected)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun VARangeSlider(modifier: Modifier,
                      label: String,
                      rangeSliderState: RangeSliderState, onNewRangeSelected: (Float, Float) -> Unit){

        rangeSliderState.onValueChangeFinished =
            { onNewRangeSelected(rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd) }
        VARangeSlider(modifier = modifier, label = label, rangeSliderState = rangeSliderState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun VARangeSlider(
        modifier: Modifier,
        label: String,
        rangeSliderState: RangeSliderState,
    ) {
        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }


        Column(modifier = modifier) {
            Text(text = label.format(rangeSliderState.activeRangeStart.toInt(), rangeSliderState.activeRangeEnd.toInt()))
            Spacer(Modifier.padding(8.dp))
            Column {
                RangeSlider(
                    state = rangeSliderState,
                    modifier = Modifier.semantics { contentDescription = "Localized Description" },
                    startInteractionSource = startInteractionSource,
                    endInteractionSource = endInteractionSource,
                    startThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = startInteractionSource,
                        )
                    },
                    track = { rangeSliderState ->
                        SliderDefaults.Track(
                            rangeSliderState = rangeSliderState
                        )
                    }
                )
            }
        }
    }


    @Composable
    fun VACarList(
        uiState: CarListUiState,
        innerPadding: PaddingValues,
        selectedPage: Int,
        goToDetails: (Int) -> Unit
    ) {
        Column(Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)) {
            if(uiState.paginatedCars == null){
                Text(stringResource(R.string.no_cars))
                return@Column
            }
            CarList(cars = uiState.paginatedCars[selectedPage].toMap(),
                onItemClick = goToDetails)
        }
    }

    @SuppressLint("DefaultLocale")
    @Composable
    fun PageSelector(numberOfPages: Int, selectedPage: Int, resultsPerPage: Int, totalResults: Int, selectPage: (Int) -> Unit) {
        val resultsTo = totalResults.div(numberOfPages).times(selectedPage + 1)
        val resultsFrom = (resultsTo - resultsPerPage).coerceAtLeast(0) + 1
        Column {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                items(numberOfPages) { index ->
                    val textColor = if(selectedPage == index) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary

                    Box(
                        modifier = Modifier
                            .fillMaxWidth().padding(2.dp)
                            .background(if(selectedPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                            .clip(RoundedCornerShape(2.dp))
                            .border(BorderStroke(1.dp, textColor))
                            .clickable { selectPage(index) },
                    ) {
                        Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = textColor,
                            text = (index + 1).toString())
                    }
                }
            }
            VAText(modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = String.format("Results: %s - %s of %s", resultsFrom, resultsTo, totalResults),
                textAlign = TextAlign.End, maxLines = 1)
        }
    }

    @Composable
    fun CarList(cars: Map<Int, Car>, onItemClick: (Int) -> Unit){
        val state = rememberScrollState()
        Column {
            Column(Modifier.verticalScroll(state)) {
                cars.forEach{ item ->
                    CarListItem(item = item, onItemClick = onItemClick)
                }
            }
        }
    }

    @Composable
    fun CarListItem(item: Map.Entry<Int, Car>, onItemClick: (Int) -> Unit) {
        val car = item.value

        Column(modifier =
        Modifier
            .padding(16.dp).clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onItemClick(item.key) }){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)){
                Image(bitmap = ImageBitmap.imageResource(R.drawable.placeholder),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Car Image")
                Icon(if(car.favourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    tint = Color.Red,
                    contentDescription = "Favourite")
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                VATitleText(modifier = Modifier.fillMaxWidth(0.7f).padding(horizontal = 16.dp),
                    text = car.shortTitle(),
                    textAlign = TextAlign.Start)
                VATitleText(modifier = Modifier.padding(horizontal = 16.dp),
                    text = String.format("%s €", car.startingBid),
                    textAlign = TextAlign.End)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {
                VAIconText(modifier = Modifier.padding(horizontal = 16.dp),
                    icon = ImageVector.vectorResource(R.drawable.baseline_access_time_24),
                    text = car.year.toString())
                VAIconText(modifier = Modifier.padding(horizontal = 16.dp),
                    icon = ImageVector.vectorResource(R.drawable.baseline_speed_24),
                    text = String.format(stringResource(R.string.miles_format), car.mileage))
            }
            HorizontalDivider(Modifier.padding(4.dp))
            Row(Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable { onItemClick(item.key) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                VAText(modifier = Modifier.padding(horizontal = 16.dp), text ="Auction Time:")
                VADateText(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = car.auctionDateTime
                )
            }
            Spacer(Modifier.padding(8.dp))
        }
    }
}

@Composable
fun Loading(label: String){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(label, Modifier.padding(36.dp))
    }
}

@Composable
fun VAIconText(modifier: Modifier, text: String, icon: ImageVector) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon,
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.surfaceTint,
            contentDescription = "Favourite")
        VAText(modifier = Modifier,
            text = text,
            textAlign = TextAlign.Start)
    }

}

@Composable
fun VAText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    textStyle: TextStyle = TextStyle.Default,
    maxLines: Int = 1
) {
    val default = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.SansSerif,
        fontSize = TextUnit(value = 16f, type = TextUnitType.Sp))

    Text(modifier = modifier,
        text = text,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        style = default.merge(textStyle),
        textAlign = textAlign)
}

@Composable
fun VADateText(modifier: Modifier, text: String) {
     VAText(modifier = modifier, text = getFormattedDate(text))
}

fun getFormattedDate(
    timestamp: String,
): String {
    val timestampFormat = "yyyy/MM/dd HH:mm:ss"
    val outputFormat = "MMMM dd, yyyy | hh:mma"

    val dateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("GMT")

    val parser = SimpleDateFormat(timestampFormat, Locale.getDefault())
    parser.timeZone = TimeZone.getTimeZone("GMT")

    try {
        val date = parser.parse(timestamp)
        if (date != null) {
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(date)
        }
    } catch (e: Exception) {
        // Handle parsing error
        e.printStackTrace()
    }

    // If parsing fails, return the original timestamp
    return timestamp
}

@Composable
fun VATitleText(modifier:Modifier = Modifier, text: String, textAlign: TextAlign = TextAlign.Start) {
    val textStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = TextUnit(value = 16f, type = TextUnitType.Sp))

    VAText(modifier = modifier,
        text = text,
        textStyle = textStyle,
        textAlign = textAlign)
}

@Composable
fun VABigTitleText(modifier:Modifier = Modifier, text: String, textAlign: TextAlign = TextAlign.Start) {
    val textStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = TextUnit(value = 24f, type = TextUnitType.Sp))

    VAText(modifier = modifier,
        text = text,
        textStyle = textStyle,
        textAlign = textAlign)
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview(){
    Loading("Loading")
}

@Preview(showBackground = true)
@Composable
fun RangeSliderPreview(){
    VARangeSlider(modifier = Modifier, minValue = 0f, maxValue = 100f, label = "Range between %s and %s", onNewRangeSelected = { _,_ ->} )
}

@Preview(showBackground = true)
@Composable
fun PageSelectorPreview(){
    PageSelector(numberOfPages = 10,
        selectedPage = 1,
        totalResults = 500,
        resultsPerPage = 50,
        selectPage = {_ ->})
}

@Preview(showBackground = true)
@Composable
fun CarListItemPreview(){
    val entry = mapOf(Pair(0, Car.sample)).entries.first()
    CarListItem(entry) {}
}

@Preview(showBackground = true)
@Composable
fun CarListPreview(){
    val cars = mapOf(
        Pair(0, Car.sample), Pair(1, Car.sample), Pair(2, Car.sample2),
        Pair(3, Car.sample), Pair(4, Car.sample2), Pair(5, Car.sample),
        Pair(6, Car.sample2), Pair(7, Car.sample), Pair(8, Car.sample2),
        Pair(9, Car.sample), Pair(10, Car.sample), Pair(11, Car.sample), Pair(12, Car.sample2),
        Pair(13, Car.sample), Pair(14, Car.sample2), Pair(15, Car.sample),
        Pair(16, Car.sample2), Pair(17, Car.sample), Pair(18, Car.sample2),
        Pair(19, Car.sample))

    VACarList(uiState = CarListUiState(filteredCars = cars,
        paginatedCars = listOf(cars.toList()),
        numberOfPages = 10), goToDetails = {}, innerPadding = PaddingValues.Absolute(0.dp,0.dp,0.dp,0.dp), selectedPage = 0)
}

