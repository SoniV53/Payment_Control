package com.control.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.utils.ConstantsNames.MONTH_TABLE

@Dao
interface MonthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonth(month:MonthEntity)
    @Update
    fun updateMonth(month:MonthEntity)
    @Delete
    fun deleteMonth(month:MonthEntity)
    @Query("SELECT * FROM $MONTH_TABLE ORDER BY yearsEntity ASC")
    fun getAllMonths():List<MonthEntity>
    @Query("SELECT * FROM $MONTH_TABLE WHERE yearsEntity LIKE :idYear")
    fun orderByIdYearMonth(idYear:String):List<MonthEntity>

    @Query("SELECT * FROM $MONTH_TABLE WHERE name LIKE :name AND yearsEntity LIKE :yearsEntity")
    fun orderByIdMonthAndYear(name:String,yearsEntity:String):MonthEntity
}