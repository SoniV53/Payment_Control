package com.control.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.control.roomdatabase.dao.MonthDao
import com.control.roomdatabase.dao.SpentDao
import com.control.roomdatabase.dao.YearsDao
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity

@Database(entities = [YearsEntity::class,MonthEntity::class,SpentEntity::class], version = 4)
abstract class ControlDataBase: RoomDatabase() {
    abstract fun getYearDao(): YearsDao
    abstract fun getMonthDao(): MonthDao
    abstract fun getSpentDao(): SpentDao

}