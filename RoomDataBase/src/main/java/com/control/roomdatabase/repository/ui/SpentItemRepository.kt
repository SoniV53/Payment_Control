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
import com.control.roomdatabase.utils.Status.CODE_201
import com.control.roomdatabase.utils.Status.CODE_400
import com.control.roomdatabase.utils.Status.ERROR
import com.control.roomdatabase.utils.Status.SUCCESS
import com.google.gson.Gson

class SpentItemRepository (context: Context) : BaseRepository(context) {
    private var spentDao: SpentDao = controlDataBase.getSpentDao()
    fun getAddSpent(spent:SpentEntity,isFav:Boolean = false) :  MutableLiveData<ResponseBase>{
        val response:MutableLiveData<ResponseBase> = MutableLiveData()
        val spentByTitle:SpentEntity = spentDao.getSpentByTitleAndIdMonth(spent.title,spent.idMonth)
        val isValid = (spentByTitle != null && spent.idMonth.equals(spentByTitle.idMonth))

        response.value = if (!isEmptyItem(spent.title) && !isValid) {
            getSpentFun(spent,isFav,false)
            ResponseBase(SUCCESS, CODE_200)
        }else if (isValid){
            getSpentFun(spent,isFav,true)
            ResponseBase(SUCCESS, CODE_201,"Se Actualizo Correctamente")
        }else {
            ResponseBase(ERROR, CODE_400,"Error al crear, revise sus datos")
        }

        return response
    }

    private fun getSpentFun(spent:SpentEntity,isFav:Boolean,isUpdate:Boolean){
        if (isFav){
            if (isUpdate)spentDao.updateSpent(spent)
            else spentDao.insertSpent(spent)

            val spentFav:SpentEntity = spent
            spentFav.id = 0
            spentFav.idMonth = ""
            spentFav.cancelPay = "0"
            val spentByTitleFav:SpentEntity = spentDao.getSpentByTitleAndIdMonth(spentFav.title,"")
            val isValidFav = (spentByTitleFav != null)

            if (!isEmptyItem(spentFav.title) && !isValidFav)
                spentDao.insertSpent(spentFav)
            else{
                val spentFavUpd:SpentEntity = spent
                spentFavUpd.id = spentByTitleFav.id
                spentDao.updateSpent(spentFavUpd)
            }

        }else{
            if (isUpdate)spentDao.updateSpent(spent)
            else spentDao.insertSpent(spent)
        }
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

    fun getByAllSpent() : MutableLiveData<List<SpentEntity>> {
        val response: MutableLiveData<List<SpentEntity>> = MutableLiveData()
        response.value = spentDao.getAllSpentByIdMonth("")
        //response.value = spentDao.getAllSpent()
        return response
    }

}