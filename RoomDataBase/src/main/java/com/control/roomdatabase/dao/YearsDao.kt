package com.control.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.ConstantsNames.YEAR_TABLE

@Dao
interface YearsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertYear(year:YearsEntity)
    @Update
    fun updateYear(year:YearsEntity)
    @Delete
    fun deleteYear(year: YearsEntity)
    @Query("SELECT * FROM $YEAR_TABLE ORDER BY name ASC")
    fun getAllYears():List<YearsEntity>
    @Query("SELECT * FROM $YEAR_TABLE WHERE id LIKE :idYear")
    fun orderByIdYear(idYear:String):YearsEntity

    @Query("SELECT * FROM $YEAR_TABLE WHERE name LIKE :name")
    fun orderByIdNameYear(name : String):YearsEntity
}