package com.control.roomdatabase.repository.base

import android.content.Context
import androidx.room.Room
import com.control.roomdatabase.ControlDataBase
import com.control.roomdatabase.utils.ConstantsNames

open class BaseRepository(context: Context) {
    protected val controlDataBase : ControlDataBase by lazy {
        Room.databaseBuilder(context, ControlDataBase::class.java, ConstantsNames.CONTROL_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    protected fun isEmptyItem(value:String):Boolean{
        return value == null || (value != null && value.trim().isEmpty())
    }
}