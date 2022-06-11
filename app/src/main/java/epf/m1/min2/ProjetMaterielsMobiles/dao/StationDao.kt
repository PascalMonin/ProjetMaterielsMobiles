package epf.m1.min2.ProjetMaterielsMobiles.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import epf.m1.min2.ProjetMaterielsMobiles.entity.Station

@Dao
interface StationDao {
    @Query("Select * FROM station")
    fun getAll() : List<Station>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(objects: List<Station>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(station : Station)
}