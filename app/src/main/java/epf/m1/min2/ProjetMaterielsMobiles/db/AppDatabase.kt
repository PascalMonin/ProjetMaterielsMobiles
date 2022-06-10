package epf.m1.min2.ProjetMaterielsMobiles.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import epf.m1.min2.ProjetMaterielsMobiles.dao.StationDao
import epf.m1.min2.ProjetMaterielsMobiles.entity.Station

@Database(entities = [Station::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun stationDao(): StationDao
}