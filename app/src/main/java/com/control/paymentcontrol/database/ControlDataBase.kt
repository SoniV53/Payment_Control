package com.control.paymentcontrol.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.control.paymentcontrol.database.dao.YearsDao
import com.control.paymentcontrol.database.entities.YearsEntity

@Database(entities = [YearsEntity::class], version = 1)
abstract class ControlDataBase:RoomDatabase() {
    abstract fun getYearDao():YearsDao
}