package pt.pedromarques.vehicleauction.home

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.ui.components.Components.VARangeSlider
import pt.pedromarques.vehicleauction.ui.components.Loading
import pt.pedromarques.vehicleauction.ui.components.VABigTitleText
import pt.pedromarques.vehicleauction.ui.components.VAText
import pt.pedromarques.vehicleauction.ui.theme.VehicleAuctionTheme


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    goToSearchResults: (String, String, Float, Float, Int) -> Unit,
    goToFavourites: () -> Unit
){
    val uiState by viewModel.homeSate.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        updateModelsList = { make ->
            viewModel.updateModelsByMake(make = make)
        },
        goToSearchResults = goToSearchResults,
        goToFavourites = goToFavourites)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(uiState: HomeUiState,
               updateModelsList: (String) -> Unit,
               goToSearchResults: (String, String, Float, Float, Int) -> Unit,
               goToFavourites: () -> Unit) {

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheetSelectorList by remember { mutableStateOf(false) }

    val (make, setMake) = remember {
        mutableStateOf("")
    }
    val (model, setModel) = remember {
        mutableStateOf("")
    }
    var selectedMinBid by remember { mutableFloatStateOf(uiState.startBidValue) }
    var selectedMaxBid by remember { mutableFloatStateOf(uiState.endBidValue) }
    var itemsPerPageSelected by remember { mutableIntStateOf(10) }

    LaunchedEffect(make) {
        updateModelsList(make)
    }
    
    val toggleModalBottomSheetState = {
        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheetSelectorList = !showBottomSheetSelectorList
        }
    }

    var selector: SelectorType = SelectorType.Make

    VehicleAuctionTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (uiState.isLoading) {
                    Loading(stringResource(R.string.loading_cars))

                    return@Column
                }

                VABigTitleText(modifier = Modifier.fillMaxWidth().padding(24.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.cars_for_auction_title))

                VAText(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.search_description))
                Filters(
                    selectedMaker = make,
                    showMakerList = {
                        selector = SelectorType.Make
                        toggleModalBottomSheetState()
                    },
                    selectedModel = model,
                    showModelList = {
                        selector = SelectorType.Model
                        toggleModalBottomSheetState()
                    },
                    startBidValue = uiState.startBidValue, endBidValue = uiState.endBidValue,
                    newBidRangeSelected = { min, max ->
                        selectedMinBid = min
                        selectedMaxBid = max
                    },
                    goToFavourites = goToFavourites,
                    goToSearchResults = {
                        goToSearchResults(
                            make,
                            model,
                            selectedMinBid,
                            selectedMaxBid,
                            itemsPerPageSelected
                        )
                    },
                    itemsPerPageSelected = { i -> itemsPerPageSelected = i },
                    onClearMake = { setMake(""); setModel("") },
                    onClearModel = { setModel("") }
                )
            }
            if (showBottomSheetSelectorList) {
                if (selector == SelectorType.Make) {
                    BottomSheet(sheetState, toggleModalBottomSheetState, setMake, uiState.makers)
                } else {
                    BottomSheet(sheetState, toggleModalBottomSheetState, setModel, uiState.models)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    toggleModalBottomSheetState: () -> DisposableHandle,
    setValue: (String) -> Unit,
    items: List<String>
) {
    ModalBottomSheet(
        modifier = Modifier.testTag("modal"),
        onDismissRequest = { toggleModalBottomSheetState() },
        sheetState = sheetState
    ) {
        // Sheet content
        LazyColumn {
            items(items.size) {
                Text(
                    text = items[it],
                    modifier = Modifier.testTag("modalItem_$it")
                        .fillMaxWidth()
                        .clickable {
                            setValue(items[it])
                            toggleModalBottomSheetState()
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp,
                        ),
                )
            }
        }
        Button(modifier = Modifier.padding(8.dp), onClick = {
            toggleModalBottomSheetState()
        }) {
            VAText(modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.dismiss))
        }
    }
}

enum class SelectorType {
    Make, Model
}

@Composable
fun Filters(
    selectedMaker: String, showMakerList: () -> Unit,
    selectedModel: String, showModelList: () -> Unit,
    startBidValue: Float, endBidValue: Float,
    newBidRangeSelected: (Float, Float) -> Unit,
    goToSearchResults: () -> Unit,
    goToFavourites: () -> Unit,
    itemsPerPageSelected: (Int) -> Unit,
    onClearMake: () -> Unit,
    onClearModel: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        VAReadOnlyTextField(value = selectedMaker, label = stringResource(R.string.make),
            onClick = { showMakerList() },
            onDeleteClick = onClearMake)
        VAReadOnlyTextField(value = selectedModel, label = stringResource(R.string.model),
            onClick = { if(selectedMaker.isNotEmpty()) {
                showModelList()
            } }, onDeleteClick = onClearModel)
        VARangeSlider(
            modifier = Modifier.padding(8.dp),
            minValue = startBidValue,
            maxValue = endBidValue,
            label = stringResource(R.string.range_format),
            onNewRangeSelected = newBidRangeSelected)
        VAText(modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.number_of_cars_per_page))
        SingleChoiceSegmentedButton(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            selected = itemsPerPageSelected)
        Spacer(Modifier.padding(16.dp))
        Button(modifier = Modifier.fillMaxWidth(),onClick = goToSearchResults) {
            Text(stringResource(R.string.search))
        }

        HorizontalDivider(Modifier.padding(16.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = goToFavourites) {
            Text(stringResource(R.string.see_favourites))
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier, selected: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(10, 25, 50, 100)

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex = index
                    selected(options[index])
                          },
                selected = index == selectedIndex,
                label = { Text(label.toString()) }
            )
        }
    }
}

@Composable
fun VAReadOnlyTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        VATextField(modifier = modifier,
            value = value,
            label = label,
            onDeleteClick = onDeleteClick)
        Box(
            modifier = Modifier
                .matchParentSize().padding(end = 48.dp)
                .alpha(0f)
                .clickable(
                    onClick = onClick,
                ),
        )
    }
}

@Composable
fun VATextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = label,
                )
            },
            trailingIcon = {
                if(value.isNotEmpty()) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = ""
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(uiState = HomeUiState(isLoading = false), updateModelsList = {}, goToSearchResults = { _, _, _, _, _ -> }, goToFavourites = {})
}