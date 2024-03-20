package com.control.paymentcontrol.ui.payment.detailsPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.adapter.AdapterDataPaymentTable
import com.control.paymentcontrol.component.constraint.BackgroundCalendar
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.utils.Status
import com.google.gson.Gson

class ControllerDataMovements(
    var calendar:BackgroundCalendar,
    var monthItem: MonthEntity,
    var viewModel: ServicePaymentViewModel,
    var requiredActivity: FragmentActivity,
    var fragment: Fragment,
    var baseFragment: BaseFragment,
    var gson: Gson = Gson(),
    var idNav:Int = 0) {

    lateinit var spentList: List<SpentEntity>

    var payMonthTotal = 0.00
    var calcPayMonthTotal = 0.00
    var totalMonth = ""

    lateinit var adapterPay: AdapterDataPayment
    lateinit var adapterMo: AdapterDataPaymentTable
    private var format = FormatsMoney()


    private fun  init(){
        calcDetails()

        calendar.getBindingCalendar().txtTitle.text = monthItem.name
        totalMonth = monthItem.total

        calendar.getBindingCalendar().tableCardResults.visibility = View.VISIBLE
        calendar.getBindingCalendar().txtPres.text = format.formatCurrency(monthItem?.total.toString())
        calendar.getBindingCalendar().txtGas.text = format.formatCurrency(monthItem?.payTotalMonth.toString())

        val rest = if (monthItem?.payTotalMonth.toString().isNotEmpty() && monthItem?.total.toString().isNotEmpty())
            monthItem?.total.toString().toDouble() - monthItem?.payTotalMonth.toString().toDouble() else 0.0
        calendar.getBindingCalendar().txtRest.text = format.formatCurrency(rest.toString())
    }

    fun calcDetails(){
        payMonthTotal = 0.0

        calcPayMonthTotal = 0.0
        if (spentList.size > 0){
            spentList.forEach{item ->
                payMonthTotal += item.amount.toDouble()
            }

        }
    }

    /**
     * Servicio que trae los gastos
     */
    fun getOrderSpent(){
        viewModel.fullBySpent(requiredActivity,monthItem.id.toString()).observe(requiredActivity) {responseBase ->
            if (responseBase != null) {
                spentList = responseBase.spent
                recyclerViewData()
                setUpdateMonth()
                calendar.getBindingCalendar().empty.visibility = if (spentList.isEmpty()) View.VISIBLE else View.GONE
            }else{
                calendar.getBindingCalendar().empty.visibility = View.VISIBLE
            }

        }
    }

    /**
     * Servicio para eliminar Gasto
     */
    private fun deleteSpent(item:SpentEntity){
        viewModel.deleteSpentStatus(requiredActivity,item).observe(requiredActivity) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                baseFragment.dialogMessageTitle(baseFragment.getStringRes(R.string.success_delete))
                getOrderSpent()
            }else{
                baseFragment.dialogMessageDefault(baseFragment.getStringRes(R.string.error),responseBase.message,
                    1
                )
            }
        }
    }

    /**
     * Update Month CONTROLLER
     */
    private fun setUpdateMonth(value:String = "",isMessage:Boolean = false){
        calcDetails()
        val updateMonth = monthItem
        updateMonth.total = value.ifEmpty { monthItem.total }
        updateMonth.payTotalMonth = payMonthTotal.toString()
        viewModel.setUpdateMonthDataBase(requiredActivity, updateMonth).observe(requiredActivity) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                if (isMessage)
                    baseFragment.dialogMessageTitle("SE ACTUALIZO")
                init()
            }else{
                baseFragment.dialogMessageDefault(baseFragment.getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }

    private fun recyclerViewData(){
        adapterMo = AdapterDataPaymentTable(viewModel.getListMovementSpent(spentList),requiredActivity,object : AdapterDataPaymentTable.OnClickButton{
            override fun onEdit(item: SpentEntity) {
                if (idNav != 0){
                    val bundle = Bundle()
                    bundle.putString(PutArgumentsString.MONTH_SELECT,gson.toJson(monthItem))
                    bundle.putString(PutArgumentsString.PAYMENT_SELECT,gson.toJson(item))
                    bundle.putInt(PutArgumentsString.TYPE_ENT,1)
                    bundle.putBoolean(PutArgumentsString.TYPE_EDIT,true)
                    fragment.findNavController().navigate(idNav,bundle)
                }
                //fragment.findNavController().navigate(R.id.action_detailsMovementPayFragment_to_formularyPaymentFragment,bundle)
            }

            override fun onDelete(item: SpentEntity) {
                baseFragment.dialogMessageOnAction(baseFragment.getStringRes(R.string.delete),
                    baseFragment.getStringRes(R.string.delete_message),1,baseFragment.getStringRes(R.string.delete),object:
                        OnClickInterface {
                        override fun onClickAction() {
                            deleteSpent(item)
                        }

                    })
            }

        })

        calendar.getBindingCalendar().reciclerView.layoutManager = LinearLayoutManager(requiredActivity)
        calendar.getBindingCalendar().reciclerView.adapter = adapterMo

    }


}