package epf.m1.min2.ProjetMaterielsMobiles.api

import java.util.*

class RetroModel(
    var station_id: Long,
    var name: String,
    var lat:Double,
    var lon: Double,
    var capacity: Int,
    var stationCode: String,
    var is_installed: Boolean = false,
    var is_renting: Boolean = false,
    var is_returning: Boolean = false,
    val last_reported: String,
    var numBikesAvailable: Int = 0,
    val numDocksAvailable: Int = 0,
    val num_bikes_available: Int = 0,
    val num_bikes_available_types: String,
    val liked:Boolean = false
)