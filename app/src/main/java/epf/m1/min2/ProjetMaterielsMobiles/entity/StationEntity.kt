package epf.m1.min2.ProjetMaterielsMobiles.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["lat","lon"])
data class Station(
    var lat: Double,
    val lon: Double
    )