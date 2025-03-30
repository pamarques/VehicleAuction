package pt.pedromarques.vehicleauction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import pt.pedromarques.vehicleauction.cars.CarDetailScreen
import pt.pedromarques.vehicleauction.cars.CarListScreen
import pt.pedromarques.vehicleauction.cars.FavouritesScreen
import pt.pedromarques.vehicleauction.home.HomeScreen
import pt.pedromarques.vehicleauction.ui.theme.VehicleAuctionTheme

class MainActivity : ComponentActivity() {
    @Serializable
    object Home
    @Serializable
    data class CarListFilter(val make: String, val model: String, val startBid: Float, val endBid: Float, val itemsPerPage: Int)
    @Serializable
    data class CarId(val id: Int)
    @Serializable
    object UserFavourites

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VehicleAuctionTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        HomeScreen(
                            goToSearchResults = { make, model, startBid, endBid,  itemsPerPage ->
                                navController.navigate(route = CarListFilter(make = make, model = model, startBid = startBid, endBid = endBid, itemsPerPage = itemsPerPage))
                            },
                            goToFavourites = {
                                navController.navigate(route = UserFavourites)
                            }
                        ) }
                    composable<CarListFilter> {
                        CarListScreen(
                            onNavigateBack = { navController.popBackStack() },
                            goToDetails = { id ->
                                navController.navigate(route = CarId(id = id))
                            }
                    ) }
                    composable<CarId> {
                        CarDetailScreen(onNavigateBack = { navController.popBackStack() })
                    }
                    composable<UserFavourites> {
                        FavouritesScreen(onNavigateBack = { navController.popBackStack() },
                            goToDetails = { id ->
                                navController.navigate(route = CarId(id = id))
                            })
                    }
                }
            }
        }
    }
}
