package com.control.roomdatabase.repository.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.control.roomdatabase.dao.SpentDao
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.entities.embeddedWith.MonthWithSpent
import com.control.roomdatabase.repository.base.BaseRepository
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.utils.Status.CODE_200
import com.control.roomdatabase.utils.Status.CODE_400
import com.control.roomdatabase.utils.Status.ERROR
import com.control.roomdatabase.utils.Status.SUCCESS

class SpentItemRepository (context: Context) : BaseRepository(context) {
    private var spentDao: SpentDao = controlDataBase.getSpentDao()
    fun getAddSpent(spent:SpentEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()

        response.value = if (!isEmptyItem(spent.description)) {
            spentDao.insertSpent(spent)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400,"Error al crear, revise sus datos")
        }

        return response
    }

    fun getDeleteSpent(spent:SpentEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        response.value = if (spent != null) {
            spentDao.deleteSpent(spent)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400)
        }

        return response
    }
    fun updateAddSpent(spent:SpentEntity) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()

        response.value = if (!isEmptyItem(spent.description)) {
            spentDao.updateSpent(spent)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400,"Error al crear, revise sus datos")
        }

        return response
    }

    fun updateAddSpentStatus(id:Int,cancel:String) :  MutableLiveData<ResponseBase>{
        val sp = spentDao.getSpentData(id)
        sp.cancelPay = cancel
        val response:MutableLiveData<ResponseBase> = MutableLiveData()

        response.value = if (sp != null) {
            spentDao.updateSpent(sp)
            ResponseBase(SUCCESS, CODE_200)
        }else {
            ResponseBase(ERROR, CODE_400,"Error al Actualizar")
        }

        return response
    }

    fun getByAllSpentMonth(idMonth:String) : MutableLiveData<MonthWithSpent> {
        val response: MutableLiveData<MonthWithSpent> = MutableLiveData()
        response.value = spentDao.getAllWithSpent(idMonth)
        return response
    }


}