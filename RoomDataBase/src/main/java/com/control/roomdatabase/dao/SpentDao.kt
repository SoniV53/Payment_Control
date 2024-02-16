package com.control.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.embeddedWith.MonthWithSpent
import com.control.roomdatabase.utils.ConstantsNames.MONTH_TABLE

@Dao
interface SpentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpent(spent: SpentEntity)

    @Update
    fun updateSpent(spent: SpentEntity)
    @Delete
    fun deleteSpent(spent: SpentEntity)

    @Transaction
    @Query("SELECT * FROM $MONTH_TABLE WHERE id LIKE :idMonth")
    fun getAllWithSpent(idMonth:String): List<MonthWithSpent>

}