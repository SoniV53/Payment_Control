package com.control.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.utils.ConstantsNames
import com.control.roomdatabase.utils.ConstantsNames.MONTH_TABLE
import com.control.roomdatabase.utils.ConstantsNames.YEAR_TABLE

@Dao
interface MonthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonth(month:MonthEntity)
    @Update
    fun updateMonth(month:MonthEntity)
    @Delete
    fun deleteMonth(month:MonthEntity)
    @Query("SELECT * FROM $MONTH_TABLE ORDER BY idYear ASC")
    fun getAllMonths():List<MonthEntity>
    @Query("SELECT * FROM $MONTH_TABLE WHERE idYear LIKE :idYear")
    fun orderByIdYearMonth(idYear:String):List<MonthEntity>

    @Query("SELECT * FROM $MONTH_TABLE WHERE name LIKE :name AND idYear LIKE :yearsEntity")
    fun orderByIdMonthAndYear(name:String,yearsEntity:String):MonthEntity

    @Query(
        "SELECT * FROM $MONTH_TABLE " +
        "INNER JOIN $YEAR_TABLE ON $YEAR_TABLE.id = $MONTH_TABLE.idYear " +
        "WHERE $YEAR_TABLE.id LIKE :idYear")
    fun getByMonthIdYear(idYear:String):List<MonthEntity>

    @Query(
        "SELECT * FROM $YEAR_TABLE " +
                "INNER JOIN $MONTH_TABLE ON $MONTH_TABLE.idYear = $YEAR_TABLE.id " +
                "WHERE $MONTH_TABLE.idYear LIKE :idYear")
    fun getByMonthIdYears(idYear:String):List<MonthEntity>
}