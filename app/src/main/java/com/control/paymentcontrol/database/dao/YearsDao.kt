package com.control.paymentcontrol.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.control.paymentcontrol.database.utils.ConstantsTables.YEAR_TABLE
import com.control.paymentcontrol.database.entities.YearsEntity

@Dao
interface YearsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertYear(year:YearsEntity)
    @Update
     fun updateYear(year:YearsEntity)
    @Delete
     fun deleteYear(year:YearsEntity)
    @Query("SELECT * FROM $YEAR_TABLE ORDER BY name DESC")
     fun getAllYears():List<YearsEntity>
    @Query("SELECT * FROM $YEAR_TABLE WHERE id LIKE :idYear")
     fun orderByIdYear(idYear : Int):YearsEntity
}