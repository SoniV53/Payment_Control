package com.control.roomdatabase.repository.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.control.roomdatabase.dao.MonthDao
import com.control.roomdatabase.dao.YearsDao
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.base.BaseRepository
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.utils.Status.CODE_200
import com.control.roomdatabase.utils.Status.CODE_400
import com.control.roomdatabase.utils.Status.CODE_401
import com.control.roomdatabase.utils.Status.ERROR
import com.control.roomdatabase.utils.Status.SUCCESS

class MonthItemRepository (context: Context) : BaseRepository(context) {
    private var monthDao: MonthDao = controlDataBase.getMonthDao()
    private var yearDao: YearsDao = controlDataBase.getYearDao()

    fun getByAllMonths() : MutableLiveData<List<MonthEntity>>{
        val response:MutableLiveData<List<MonthEntity>> = MutableLiveData()
        response.value = if (monthDao.getAllMonths() != null) monthDao.getAllMonths() else ArrayList()
        return response
    }

    fun getByAllMonthIdYear(idYear:String) : MutableLiveData<List<MonthEntity>> {
        val response: MutableLiveData<List<MonthEntity>> = MutableLiveData()
        response.value = monthDao.orderByIdYearMonth(idYear) ?: ArrayList()
        response.value = orderMonth(response.value!!)
        return response
    }

    private fun orderMonth(monthList: List<MonthEntity>): List<MonthEntity> {
        val newMonths = mutableListOf<MonthEntity>()
        val orderMonth = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")

        orderMonth.forEach { month ->
            monthList.forEach { itemResponse ->
                if (month == itemResponse.name) {
                    newMonths.add(itemResponse)
                }
            }
        }


        return newMonths
    }

    fun getAddMonth(month:MonthEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        val monthName:MonthEntity = monthDao.orderByIdMonthAndYear(month.name.toString(),month.yearsEntity)
        val yearId:YearsEntity = yearDao.orderByIdYear(month.yearsEntity)

        response.value = if (monthName == null && yearId != null && !isEmptyItem(month.name.toString())) {
            monthDao.insertMonth(month)
            ResponseBase(SUCCESS, CODE_200)
        }else if(isEmptyItem(month.name.toString())) {
            ResponseBase(ERROR, CODE_401,"Seleccione un Mes")
        }else if(monthName != null){
                ResponseBase(ERROR, CODE_401,"Este Mes ya fue agregado")
        }else {
            ResponseBase(ERROR, CODE_400,"El Año a agregar no existe")
        }

        return response
    }

    fun getDeleteMonth(month:MonthEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        response.value = if (month != null) {
            monthDao.deleteMonth(month)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400)
        }

        return response
    }

}