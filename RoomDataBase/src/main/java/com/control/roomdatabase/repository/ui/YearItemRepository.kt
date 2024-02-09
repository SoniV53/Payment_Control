package com.control.roomdatabase.repository.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.control.roomdatabase.dao.YearsDao
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.base.BaseRepository
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.utils.Status.CODE_200
import com.control.roomdatabase.utils.Status.CODE_400
import com.control.roomdatabase.utils.Status.CODE_401
import com.control.roomdatabase.utils.Status.ERROR
import com.control.roomdatabase.utils.Status.SUCCESS

class YearItemRepository (context: Context) : BaseRepository(context) {
    private var yearDao: YearsDao = controlDataBase.getYearDao()

    fun getByAllYears() : List<YearsEntity>{
        return if (yearDao.getAllYears() != null) yearDao.getAllYears() else ArrayList()
    }

    fun orderByIdNameYear(name:String) : YearsEntity{
        return yearDao.orderByIdNameYear(name)
    }

    fun getAddYear(year:YearsEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        val yearName:YearsEntity = orderByIdNameYear(year.name.toString())

        response.value = if (yearName != null && !isEmptyItem(year.name.toString())) {
            yearDao.insertYear(year)
            ResponseBase(SUCCESS, CODE_200)
        }else if(isEmptyItem(year.name.toString())) {
            ResponseBase(ERROR, CODE_401)
        }else {
            ResponseBase(ERROR, CODE_400)
        }

        return response
    }
}