package com.control.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.control.roomdatabase.dao.YearsDao
import com.control.roomdatabase.entities.YearsEntity

@Database(entities = [YearsEntity::class], version = 1)
abstract class ControlDataBase: RoomDatabase() {
    abstract fun getYearDao(): YearsDao
}