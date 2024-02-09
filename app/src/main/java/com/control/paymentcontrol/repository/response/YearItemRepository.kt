package com.control.paymentcontrol.repository.response

import android.content.Context
import com.control.paymentcontrol.database.dao.YearsDao
import com.control.paymentcontrol.database.entities.YearsEntity
import com.control.paymentcontrol.repository.BaseRepository
import com.control.paymentcontrol.repository.ResponseBase

class YearItemRepository(context: Context) : BaseRepository(context){
    private var yearDao: YearsDao = controlDataBase.getYearDao()

    fun getByAllYears() : List<YearsEntity>{
        return if (yearDao.getAllYears() != null) yearDao.getAllYears() else ArrayList()
    }

    fun getAddYear(year:YearsEntity) : ResponseBase {
        val response = ResponseBase("SUCCESS","000")
        yearDao.insertYear(year)
        return response
    }
}