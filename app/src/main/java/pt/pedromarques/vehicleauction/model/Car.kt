package pt.pedromarques.vehicleauction.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.NamedCompanion
import java.util.Date
import java.util.Objects

@Serializable
data class Car (
    val make: String,
    val model: String,
    val engineSize: String,
    val fuel: String,
    val year: Int,
    val mileage: Int,
    val auctionDateTime: String,
    val startingBid: Int,
    var favourite: Boolean,
    val details: CarDetail? = null
) {
    fun shortTitle(): String {
        return "$make $model"
    }

    companion object {
        val sample = Car(make = "Toyota", model = "C-HR", engineSize = "1.8L", fuel = "diesel", year = 2022,
            mileage = 743, auctionDateTime = "2024/04/15 09:00:00", startingBid =  17000, favourite = true, details = CarDetail.sample)
        val sample2 = Car(make = "Ford", model = "Fiesta", engineSize = "1.8L", fuel = "diesel", year = 2022,
            mileage = 9084, auctionDateTime = "2024/04/15 09:00:00", startingBid =  15000, favourite = false, details = CarDetail.sample)
        val sample3 = Car(make = "Mercedes-Benz xpto screen breaker", model = "A-Class Saloon", engineSize = "1.6L", fuel = "petrol", year = 2023,
            mileage = 16245, auctionDateTime = "2024/04/15 09:00:00", startingBid =  140000, favourite = true, details = CarDetail.sample)
    }
}

@Serializable
data class CarDetail(
    val specification: CarSpecification,
    val ownership: Ownership,
    val equipment: List<String>
){
    companion object {
        val sample = CarDetail(specification = CarSpecification.sample, ownership = Ownership.sample,
            equipment = listOf(
                "Air Conditioning",
                "Tyre Inflation Kit",
                "Photocopy of V5 Present",
                "Navigation HDD",
                "17 Alloy Wheels",
                "Engine Mods/Upgrades",
                "Modifd/Added Body Parts")
        )
    }
}

@Serializable
data class CarSpecification(
    val vehicleType: String,
    val colour: String,
    val fuel: String,
    val transmission: String,
    val numberOfDoors: Int,
    val co2Emissions: String,
    val noxEmissions: Int,
    val numberOfKeys: Int
)
{
    companion object{
        val sample = CarSpecification(
            vehicleType = "Car",
            colour = "RED",
            fuel = "petrol",
            transmission = "Manual",
            numberOfDoors = 3,
            co2Emissions = "139 g/km",
            noxEmissions = 230,
            numberOfKeys = 2
        )
    }
}

@Serializable
data class Ownership(
    val logBook: String,
    val numberOfOwners: Int,
    val dateOfRegistration: String
)
{
    companion object{
        val sample = Ownership("Present", numberOfOwners = 8, dateOfRegistration = "2015/03/31 09:00:00")
    }
}


