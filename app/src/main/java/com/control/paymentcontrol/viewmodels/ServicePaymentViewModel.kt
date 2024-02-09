package com.control.paymentcontrol.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.repository.ui.YearItemRepository

class ServicePaymentViewModel : ViewModel() {
    private lateinit var addYearDataBase: MutableLiveData<ResponseBase>

    fun setAddYearDataBase(context: Context,year: YearsEntity){
        val repository = YearItemRepository(context)
        addYearDataBase = repository.getAddYear(year)
    }

    fun getAddYearDataBase() : MutableLiveData<ResponseBase>{
       return addYearDataBase
    }
}