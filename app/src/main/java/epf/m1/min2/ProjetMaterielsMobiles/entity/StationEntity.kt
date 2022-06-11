package epf.m1.min2.ProjetMaterielsMobiles.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station(
    var lat:Double,
    var lon: Double,
    @PrimaryKey var station_id: Long,
    var name: String,
    var capacity: Int,
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