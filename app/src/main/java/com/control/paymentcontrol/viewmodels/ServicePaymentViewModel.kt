package com.control.paymentcontrol.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.repository.ui.MonthItemRepository
import com.control.roomdatabase.repository.ui.YearItemRepository

class ServicePaymentViewModel : ViewModel() {
    private lateinit var addYearDataBase: MutableLiveData<ResponseBase>
    private lateinit var deleteYearDataBase: MutableLiveData<ResponseBase>
    private lateinit var getByAllYears: MutableLiveData<List<YearsEntity>>

    private lateinit var addMonthDataBase: MutableLiveData<ResponseBase>
    private lateinit var deleteMonthDataBase: MutableLiveData<ResponseBase>
    private lateinit var getByAllMonths: MutableLiveData<List<MonthEntity>>

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

    fun setDeleteYearDataBase(context: Context,year: YearsEntity){
        val repository = YearItemRepository(context)
        deleteYearDataBase = repository.getDeleteYear(year)
    }
    fun getDeleteYearDataBase() : MutableLiveData<ResponseBase>{
       return deleteYearDataBase
    }


    fun fullByYears(context: Context) : MutableLiveData<List<YearsEntity>>{
        val repository = YearItemRepository(context)
        getByAllYears = repository.getByAllYears()
        return getByAllYears
    }

    fun getYearList(context: Context):Array<String>{
        val repository = YearItemRepository(context)
        return repository.filterYearList()
    }

    /**
     * Services of Month
     */
    fun setAddMonthDataBase(context: Context,month: MonthEntity){
        val repository = MonthItemRepository(context)
        addMonthDataBase = repository.getAddMonth(month)
    }
    fun getAddMonthDataBase() : MutableLiveData<ResponseBase>{
        return addMonthDataBase
    }

    fun setDeleteMonthDataBase(context: Context,month:MonthEntity){
        val repository = MonthItemRepository(context)
        deleteMonthDataBase = repository.getDeleteMonth(month)
    }
    fun getDeleteMonthDataBase() : MutableLiveData<ResponseBase>{
        return deleteMonthDataBase
    }
    fun fullByMonths(context: Context,idYear:String) : MutableLiveData<List<MonthEntity>>{
        val repository = MonthItemRepository(context)
        getByAllMonths = repository.getByAllMonthIdYear(idYear)
        return getByAllMonths
    }

}

