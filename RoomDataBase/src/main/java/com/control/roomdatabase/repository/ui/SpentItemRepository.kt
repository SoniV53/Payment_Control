package com.control.roomdatabase.repository.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.control.roomdatabase.dao.SpentDao
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
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
            ResponseBase(ERROR, CODE_400,"Error al crear revise sus datos")
        }

        return response
    }

    fun getByAllSpentMonth(idMonth:String) : MutableLiveData<List<MonthWithSpent>> {
        val response: MutableLiveData<List<MonthWithSpent>> = MutableLiveData()
        response.value = spentDao.getAllWithSpent(idMonth) ?: ArrayList()
        return response
    }
}