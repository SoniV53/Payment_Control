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
import com.google.gson.Gson
import java.util.Calendar

class YearItemRepository (context: Context) : BaseRepository(context) {
    private var yearDao: YearsDao = controlDataBase.getYearDao()

    fun getByAllYears() : MutableLiveData<List<YearsEntity>>{
        val response:MutableLiveData<List<YearsEntity>> = MutableLiveData()
        response.value = if (yearDao.getAllYears() != null) yearDao.getAllYears() else ArrayList()
        return response
    }

    fun orderByIdNameYear(name:String) : YearsEntity{
        return yearDao.orderByIdNameYear(name)
    }

    fun getAddYear(year:YearsEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        val yearName:YearsEntity = orderByIdNameYear(year.name.toString())

        response.value = if (yearName == null && !isEmptyItem(year.name.toString())) {
            yearDao.insertYear(year)
            ResponseBase(SUCCESS, CODE_200)
        }else if(isEmptyItem(year.name.toString())) {
            ResponseBase(ERROR, CODE_401)
        }else {
            ResponseBase(ERROR, CODE_400)
        }

        return response
    }

    fun getDeleteYear(year:YearsEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        response.value = if (year != null) {
            yearDao.deleteYear(year)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400)
        }

        return response
    }

    fun filterYearList():Array<String>{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val listYear = arrayOf(addItemYear(year,0), addItemYear(year,1), addItemYear(year,2), addItemYear(year,3), addItemYear(year,4))
        val filterListYear = mutableListOf<String>()

        for (item in listYear) {
            val yearName:YearsEntity = orderByIdNameYear(item)

            if (yearName == null){
                filterListYear.add(item)
            }
        }

        return filterListYear.toTypedArray()
    }

    fun addItemYear(year:Int,sum:Int):String{
        return (year + sum).toString()
    }
}