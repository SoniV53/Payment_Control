package com.control.paymentcontrol.ui.payment

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.PutArgumentsString.ID_MONTH
import com.control.paymentcontrol.ui.utils.PutArgumentsString.MONTH_SELECT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.PAYMENT_SELECT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.TYPE_EDIT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.TYPE_ENT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.YEAR_SELECT
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status
import java.util.Arrays

class AddPaymentFragment : BaseFragment() {
    private lateinit var binding: FragmentAddPaymentBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var yearItem: YearsEntity
    private lateinit var monthItem: MonthEntity
    private lateinit var adapterPay: AdapterDataPayment
    private lateinit var spentList: List<SpentEntity>
    private var totalMonth = ""
    private var isDetails = false
    private var payMonthTotal = 0.00
    private var payMonth = 0.00
    private var calcPayMonthTotal = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddPaymentBinding.inflate(inflater,container,false)

        yearItem = gson.fromJson(arguments?.getString(YEAR_SELECT),YearsEntity::class.java)
        monthItem = gson.fromJson(arguments?.getString(MONTH_SELECT),MonthEntity::class.java)

        binding.txtTitle.text = "AÑO " + yearItem.name + " / MES " + monthItem.name
        binding.cpAmount.setText(monthItem.total)
        if (monthItem.total.isNotEmpty())
            totalMonth = binding.cpAmount.text.toString()

        onClickMoreNavbar(object: OnActionButtonNavBarMenu {
            override fun onActionPositionTwo() {
                isDetails = false
                val bundle = Bundle()
                bundle.putString(MONTH_SELECT,gson.toJson(monthItem))
                bundle.putInt(TYPE_ENT,1)
                findNavController().navigate(R.id.action_addPaymentFragment_to_formularyPaymentFragment,bundle)
            }

            override fun onActionPositionOne() {
                isDetails = false
                val bundle = Bundle()
                bundle.putInt(TYPE_ENT,1)
                bundle.putString(ID_MONTH,monthItem.id.toString())
                bundle.putBoolean(TYPE_EDIT,false)
                findNavController().navigate(R.id.action_addPaymentFragment_to_paymentFavoritesFragment,bundle)
            }

            override var attr: List<AttributesDesign>
                get() = Arrays.asList(
                    AttributesDesign(1,getStringRes(R.string.favorites_add),R.drawable.star_solid),
                    AttributesDesign(2, getStringRes(R.string.menu_add_spent),R.drawable.comment_dollar_solid)
                )
                set(value) {}
        })


        binding.consAction.setOnClickListener{
            moreDetails()
        }

        moreDetails()

        binding.cpAmount.setOnEditorActionListener { v, i, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                binding.cpAmount.clearFocus()
            }
            false
        }
        binding.cpAmount.setOnFocusChangeListener{ v, hasFocus ->
            if (!hasFocus) {
                if (binding.cpAmount.text.toString().isNotEmpty() && !totalMonth.equals(binding.cpAmount.text.toString())){
                    calcDetails(true)
                }else {
                    if (binding.cpAmount.text.toString().isEmpty()){
                        binding.cpAmount.setText(totalMonth)
                        calcDetails()
                    }
                }
                hideKeyboard(binding.root)
            }
        }

        binding.mainConstra.setOnClickListener {
            binding.cpAmount.clearFocus()
        }

        getOrderSpent()

        return binding.root
    }

    private fun recyclerViewData(){
        adapterPay = AdapterDataPayment(spentList,requireActivity(),0,object:AdapterDataPayment.OnClickButton{
            override fun onClickDelete(item: SpentEntity) {
                viewModel.deleteSpentStatus(requireActivity(),item).observe(requireActivity()) {responseBase ->
                    if (responseBase.status == Status.SUCCESS){
                        dialogMessageTitle(getStringRes(R.string.success_delete))
                        getOrderSpent()
                    }else{
                        dialogMessageDefault(getStringRes(R.string.error),responseBase.message,
                            1
                        )
                    }
                }
            }

            override fun onClickDetails(item:SpentEntity) {
                isDetails = false
                val bundle = Bundle()
                bundle.putString(MONTH_SELECT,gson.toJson(monthItem))
                bundle.putString(PAYMENT_SELECT,gson.toJson(item))
                bundle.putInt(TYPE_ENT,1)
                bundle.putBoolean(TYPE_EDIT,true)
                findNavController().navigate(R.id.action_addPaymentFragment_to_formularyPaymentFragment,bundle)
            }

            override fun onEdit(item: SpentEntity, check: Boolean) {
                viewModel.updateAddSpentStatus(requireActivity(), item)
                calcDetails()
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapterPay

    }

    /**
     * Update Month CONTROLLER
     */
    private fun setUpdateMonth(isMessage:Boolean = false){
        val updateMonth = monthItem
        updateMonth.total = binding.cpAmount.text.toString()
        updateMonth.payTotalMonth = payMonthTotal.toString()
        updateMonth.payAmount =  calcPayMonthTotal.toString()
        viewModel.setUpdateMonthDataBase(requireActivity(), updateMonth).observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                totalMonth = binding.cpAmount.text.toString()
                if (isMessage)
                    dialogMessageTitle("SE ACTUALIZO")
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }
    /**
     * Servicio que trae los gastos
     */
    private fun getOrderSpent(){
        viewModel.fullBySpent(requireActivity(),monthItem.id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                spentList = responseBase.spent
                recyclerViewData()
                calcDetails()
            }

            binding.consAction.visibility = if (spentList.size > 0) View.VISIBLE else View.GONE
            binding.empty.visibility = if (spentList.size > 0) View.GONE else View.VISIBLE
            if (spentList.isEmpty())
                binding.constMoreInfo.visibility = View.GONE
        }
    }

    private fun calcDetails(isMessage:Boolean = false){
        payMonthTotal = 0.0
        payMonth = 0.0
        calcPayMonthTotal = 0.0
        totalMonth = binding.cpAmount.text.toString()
        if (spentList.size > 0){
            spentList.forEach{item ->
                payMonthTotal += item.amount.toDouble()
                if (item.cancelPay.equals("1"))
                    payMonth += item.amount.toDouble()
            }

            binding.txtPayMonth.text = format.formatCurrency(payMonthTotal.toString())
            binding.txtPayCancel.text = format.formatCurrency(payMonth.toString())

            if (totalMonth.isNotEmpty()){
                val calcPayMonth = (totalMonth.toDouble() - payMonth)
                calcPayMonthTotal = (totalMonth.toDouble() - payMonthTotal)
                binding.txtAmountRest.text = format.formatCurrency(calcPayMonth.toString())
                binding.txtAmountRestTotal.text = format.formatCurrency(calcPayMonthTotal.toString())

                binding.txtAmountRest.setTextColor(  if (calcPayMonth > 0 ) requireActivity().getColor(R.color.primary_text) else requireActivity().getColor(R.color.error))
                binding.txtAmountRestTotal.setTextColor(  if (calcPayMonthTotal > 0 ) requireActivity().getColor(R.color.primary_text) else requireActivity().getColor(R.color.error))

            }
        }
        setUpdateMonth(isMessage)
    }

    private fun moreDetails() {
        binding.cpAmount.clearFocus()
        if (isDetails){
            binding.constMoreInfo.visibility = View.VISIBLE
            binding.imgViewDetails.setImageResource(R.drawable.chevron_up_solid)
            isDetails = false
        }else{
            binding.constMoreInfo.visibility = View.GONE
            binding.imgViewDetails.setImageResource(R.drawable.chevron_down_solid)
            isDetails = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setUpdateMonth()
    }

}