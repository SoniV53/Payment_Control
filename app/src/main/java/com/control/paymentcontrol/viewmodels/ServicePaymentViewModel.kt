package com.control.paymentcontrol.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.entities.embeddedWith.MonthWithSpent
import com.control.roomdatabase.repository.models.ResponseBase
import com.control.roomdatabase.repository.ui.MonthItemRepository
import com.control.roomdatabase.repository.ui.SpentItemRepository
import com.control.roomdatabase.repository.ui.YearItemRepository

class ServicePaymentViewModel : ViewModel() {
    private lateinit var addYearDataBase: MutableLiveData<ResponseBase>
    private lateinit var deleteYearDataBase: MutableLiveData<ResponseBase>
    private lateinit var getByAllYears: MutableLiveData<List<YearsEntity>>

    private lateinit var addMonthDataBase: MutableLiveData<ResponseBase>
    private lateinit var deleteMonthDataBase: MutableLiveData<ResponseBase>
    private lateinit var getByAllMonths: MutableLiveData<List<MonthEntity>>

    private lateinit var addSpentDataBase: MutableLiveData<ResponseBase>
    private lateinit var getByAllSpent : MutableLiveData<MonthWithSpent>
    private lateinit var getByAllSpentDataFull : MutableLiveData<List<SpentEntity>>

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

    fun setUpdateMonthDataBase(context: Context,month: MonthEntity){
        val repository = MonthItemRepository(context)
        addMonthDataBase = repository.getUpdateMonth(month)
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
        getByAllMonths = repository.getByMonthIdYear(idYear)
        return getByAllMonths
    }


    fun getValidExistMonth(listMo:Array<String>,listMonth:List<MonthEntity>):Array<String>{
        val result = mutableListOf<String>()
        listMo?.forEach { item->
            if (!isValidExistMonth(item,listMonth)){
                result.add(item)
            }
        }
        return result.toTypedArray()
    }
    private fun isValidExistMonth(value:String,listMonth:List<MonthEntity>):Boolean{
        listMonth?.forEach { item->
            if (item.name?.lowercase().equals(value.lowercase()))
                return true
        }
        return false
    }


    /**
     * Services of spent
     */
    fun setAddSpentDataBase(context: Context,spent: SpentEntity){
        val repository = SpentItemRepository(context)
        addSpentDataBase = repository.getAddSpent(spent)
    }

    fun updateAddSpentStatus(context: Context,spent: SpentEntity){
        val repository = SpentItemRepository(context)
        addSpentDataBase = repository.updateAddSpentStatus(spent.id,spent.cancelPay)
    }

    fun deleteSpentStatus(context: Context,spent: SpentEntity){
        val repository = SpentItemRepository(context)
        addSpentDataBase = repository.getDeleteSpent(spent)
    }
    fun getAddSpentDataBase() : MutableLiveData<ResponseBase>{
        return addSpentDataBase
    }

    fun fullBySpent(context: Context,idMonth:String) : MutableLiveData<MonthWithSpent>{
        val repository = SpentItemRepository(context)
        getByAllSpent = repository.getByAllSpentMonth(idMonth)
        return getByAllSpent
    }

    fun fullBySpentData(context: Context) : MutableLiveData<List<SpentEntity>>{
        val repository = SpentItemRepository(context)
        getByAllSpentDataFull = repository.getByAllSpent()
        return getByAllSpentDataFull
    }
}

