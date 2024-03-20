package com.control.paymentcontrol.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.control.paymentcontrol.models.years.YearDataModel
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.repository.ui.YearItemRepository

class ServiceYearViewModel : ViewModel() {
    private lateinit var addYearDataBase: MutableLiveData<ResponseBase>

    /**
     * Services of Year
     */
    fun setAddYearDataBase(context: Context,year: YearsEntity){
        val repository = YearItemRepository(context)
        addYearDataBase = repository.getAddYear(year)
    }
    fun getAddYearDataBase() : MutableLiveData<ResponseBase>{
       return addYearDataBase
    }

    fun getListYearFilter(listYear:List<YearsEntity>,isNew:Boolean):List<YearDataModel>{
        var list = mutableListOf<YearDataModel>()

        if (listYear != null){
            listYear.forEach{
                list.add(YearDataModel(it))
            }
        }
        if (isNew)
            list.add(YearDataModel(YearsEntity(""),1))


        return list
    }
}

