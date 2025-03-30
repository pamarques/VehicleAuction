package pt.pedromarques.vehicleauction.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.pedromarques.vehicleauction.ui.theme.VehicleAuctionTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigatedScreen(
    title: String,
    onNavigateBack: () -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    VehicleAuctionTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = snackbarHost,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(title)
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = actions,
                )
            },
            floatingActionButton = floatingActionButton,
            bottomBar = bottomBar) { innerPadding ->
            content.invoke(innerPadding)
        }
    }
}

@Preview
@Composable
fun NavigatedScreenPreview(){
    NavigatedScreen(title = "Title", onNavigateBack = {}) { }
}