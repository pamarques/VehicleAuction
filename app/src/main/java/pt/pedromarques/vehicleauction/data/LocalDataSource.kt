package pt.pedromarques.vehicleauction.data

import android.content.Context
import kotlinx.serialization.json.Json
import pt.pedromarques.vehicleauction.App
import pt.pedromarques.vehicleauction.R
import pt.pedromarques.vehicleauction.model.Car

class LocalDataSource(private val context: Context) {

    fun getCars() : List<Car> {
        context.resources.openRawResource(R.raw.vehicles_dataset).bufferedReader().use {
            val text = it.readText()
            val jsonConfig = Json {
                ignoreUnknownKeys = true
            }
            val json = jsonConfig.decodeFromString<List<Car>>(text)

           return json
        }
    }
}
