package epf.m1.min2.ProjetMaterielsMobiles.api

import java.util.*

class RetroModel(
    var station_id: Long,
    var name: String,
    var lat:Double,
    var lon: Double,
    var capacity: Int,
    var is_installed: Boolean = false,
    var is_renting: Boolean = false,
    var is_returning: Boolean = false,
    var last_reported: String,
    var numBikesAvailable: Int = 0,
    var numDocksAvailable: Int = 0,
    var num_bikes_available: Int = 0,
    var num_bikes_available_types: String,
    var liked:Boolean = false
)