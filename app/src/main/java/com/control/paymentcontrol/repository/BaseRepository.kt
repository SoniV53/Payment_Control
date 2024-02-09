package com.control.paymentcontrol.repository

import android.content.Context
import androidx.room.Room
import com.control.paymentcontrol.database.ControlDataBase
import com.control.paymentcontrol.database.utils.ConstantsTables

open class BaseRepository(context: Context) {
    protected val controlDataBase : ControlDataBase by lazy {
        Room.databaseBuilder(context, ControlDataBase::class.java, ConstantsTables.CONTROL_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

}