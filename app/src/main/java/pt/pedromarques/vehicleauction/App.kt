package pt.pedromarques.vehicleauction

import android.app.Application
import pt.pedromarques.vehicleauction.data.CarRepository
import pt.pedromarques.vehicleauction.data.CarRepositoryImpl
import pt.pedromarques.vehicleauction.data.LocalDataSource

class App : Application() {
    val repository: CarRepository = CarRepositoryImpl(LocalDataSource(this))
}