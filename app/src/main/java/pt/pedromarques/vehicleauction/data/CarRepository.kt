package pt.pedromarques.vehicleauction.data

import kotlinx.coroutines.delay
import pt.pedromarques.vehicleauction.model.Car

interface CarRepository {
    var cars: Map<Int, Car>

    suspend fun getCars(): Map<Int, Car>
    fun getCarById(carId: Int): Car? {
        return cars[carId]
    }

    fun getMakers(): List<String> {
        return cars.values.map { c -> c.make }.distinct().toList()
    }

    fun getModels(): List<String> {
        return cars.values.map { c -> c.model }.distinct().toList()
    }

    fun getModelsByMake(make: String): List<String> {
        return cars.values.filter { c -> c.make == make }.map { c -> c.model }.distinct().toList()
    }

    fun getMinStartBid(): Float {
        return cars.values.minBy { c-> c.startingBid }.startingBid.toFloat()
    }

    fun getMaxStartBid(): Float {
        return cars.values.maxBy { c-> c.startingBid }.startingBid.toFloat()
    }

    fun getFavourites(): Map<Int, Car> {
        return cars.filter { c -> c.value.favourite }
    }

    fun addFavourite(id: Int) {
        cars[id]?.favourite = true
    }

    fun removeFavourite(id: Int) {
        cars[id]?.favourite = false
    }
}

class CarRepositoryImpl(private val localDataSource: LocalDataSource) : CarRepository {
    override var cars: Map<Int, Car> = mutableMapOf()

    override suspend fun getCars(): Map<Int, Car> {
        if(cars.isEmpty()){
            delay(1000)
            val carList = localDataSource.getCars()
            var i = 0
            cars = carList.associateBy { i++ } as MutableMap<Int, Car>
        }

        return cars
    }
}

class MockCarRepositoryImpl : CarRepository {
    override var cars: Map<Int, Car> = mutableMapOf()

    override suspend fun getCars(): Map<Int, Car> {
        val carList = listOf(Car.sample, Car.sample2, Car.sample3)
        var i = 0
        cars = carList.associateBy { i++ } as MutableMap<Int, Car>
        return cars
    }
}
