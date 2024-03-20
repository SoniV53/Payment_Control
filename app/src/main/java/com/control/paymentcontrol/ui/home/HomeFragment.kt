package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.adapter.AdapterDataMonth
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.databinding.ItemFragmentDetailsBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.payment.detailsPayment.ControllerDataMovements
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.ui.utils.PutArgumentsString.MONTH_SELECT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.YEAR_SELECT
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status.SUCCESS
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.util.Arrays

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0
    private lateinit var listYear: List<YearsEntity>
    private lateinit var adapterCarrousel: AdapterCarrousel
    private lateinit var adapterCircle: AdapterCircle
    private lateinit var adapterMonth: AdapterDataMonth
    private lateinit var adapterPay: AdapterDataPayment
    private lateinit var viewModel: ServicePaymentViewModel
    private var positionCarrousel: Int = 0
    private var yearItem:YearsEntity ?= null
    private var monthItem:MonthEntity ?= null
    private lateinit var listMonth: List<MonthEntity>
    private lateinit var listMonthOriginal: List<MonthEntity>
    private lateinit var spentList: List<SpentEntity>
    private var selectMonth = false
    private lateinit var controllerMov:ControllerDataMovements



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        showOrHiddenMenuNavbar(true)
        if (!yearItem?.name.equals(getPreferenceGson()?.name))
            selectMonth = false

        yearItem = getPreferenceGson()


        onClickMoreNavbar(object:OnActionButtonNavBarMenu{
            override fun onActionPositionTwo() {
                findNavController().navigate(R.id.action_homeFragment_to_addYearFragment)
            }

            override fun onActionPositionOne() {
                val bundle = Bundle()
                bundle.putString(PutArgumentsString.ID_MONTH,"")
                findNavController().navigate(R.id.action_homeFragment_to_paymentFavoritesFragment,bundle)
            }

            override var attr: List<AttributesDesign>
                get() = Arrays.asList(AttributesDesign(1,getStringRes(R.string.favorites),R.drawable.star_solid),
                    AttributesDesign(2,getStringRes(R.string.menu_add_year),R.drawable.plus_circle_solid))
                set(value) {}
        })

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addYearFragment)
        }
        getDataInit()
        return binding.root
    }

    private fun getDataInit(){
        if (!isPreferenceData()){
            binding.iconDate.setTextTitle(yearItem?.name.toString())
            getOrderListMonth()
        }

        binding.constrainEmpty.visibility = if (isPreferenceData()) View.VISIBLE else View.GONE
        binding.mainConstra.visibility = if (!isPreferenceData()) View.VISIBLE else View.GONE
        binding.movementCalendar.getBindingCalendar().txtTitle.text = getStringRes(R.string.add)
        binding.movementCalendar.getBindingCalendar().tableCardResults.visibility = View.GONE
        binding.movementCalendar.getBindingCalendar().btnContinue.visibility = View.GONE
        if (selectMonth){
            getOrderListMonth(true)
        }
        onActionClick()
    }

    private fun onActionClick(){
        binding.iconDate.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addYearFragment)
        }

        binding.movementCalendar.getBindingCalendar().cardTitle.setOnClickListener{
            if (selectMonth){
                binding.movementCalendar.getBindingCalendar().txtTitle.text = getStringRes(R.string.add)
                selectMonth = false
                if (!isPreferenceData()){
                    getOrderListMonth()
                }
                binding.movementCalendar.getBindingCalendar().tableCardResults.visibility = View.GONE
                binding.movementCalendar.getBindingCalendar().btnContinue.visibility = View.GONE
            }else{
                getOrderNewListMonth()
            }
        }

        binding.movementCalendar.getBindingCalendar().btnContinue.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(YEAR_SELECT,gson.toJson(yearItem))
            bundle.putString(MONTH_SELECT,gson.toJson(monthItem))
            //findNavController().navigate(R.id.action_homeFragment_to_addPaymentFragment,bundle)
            findNavController().navigate(R.id.action_homeFragment_to_detailsMovementPayFragment,bundle)
        }
    }

    private fun recyclerViewMonth(){
        adapterMonth = AdapterDataMonth(listMonth,requireActivity(),object: AdapterDataMonth.OnClickButton{
            override fun onClickDelete(item: MonthEntity) {
                dialogMessageOnAction(getStringRes(R.string.delete),getStringRes(R.string.delete_message),1,getStringRes(R.string.delete),object:
                    OnClickInterface {
                    override fun onClickAction() {
                        deleteMonth(item)
                    }

                })
            }

            override fun onClickDetails(item: MonthEntity) {
                monthItem = item
                activeSelectDate()
            }

        })
        val layoutManager = GridLayoutManager(requireActivity(),3)
        binding.movementCalendar.getBindingCalendar().reciclerView.layoutManager = layoutManager
        binding.movementCalendar.getBindingCalendar().reciclerView.adapter = adapterMonth

    }

    private fun activeSelectDate(){
        selectMonth = true
        binding.movementCalendar.getBindingCalendar().btnContinue.visibility = View.VISIBLE
        controllerMov = ControllerDataMovements(
            binding.movementCalendar,
            monthItem!!,
            viewModel,
            requireActivity(),
            this,
            this,
            idNav = R.id.action_homeFragment_to_formularyPaymentFragment)

        controllerMov.getOrderSpent()

    }


    private fun getOrderListMonth(isReset:Boolean = false){
        viewModel.fullByMonths(requireActivity(),yearItem?.id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                val result = mutableListOf<MonthEntity>()
                result.addAll(responseBase)
                result.add(MonthEntity("",""))
                listMonth = result
                listMonthOriginal = responseBase
                recyclerViewMonth()

                if (isReset && monthItem != null){
                    monthItem = listMonth.filter { item ->  monthItem!!.id.equals(item.id)}.component1()
                    activeSelectDate()
                }
            }

            binding.movementCalendar.getBindingCalendar().empty.visibility = if (listMonthOriginal.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun getOrderNewListMonth(){
        var items = requireActivity().resources.getStringArray(R.array.month)
        var list = viewModel.getValidExistMonth(items,listMonth)

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(resources.getString(R.string.select_month))
            .setItems(list) { _, which ->
                setAddMonth(list[which.inc() - 1],yearItem?.id.toString())
            }
            .show()
    }

    /**
     * ADD NEW Month CONTROLLER
     */
    private fun setAddMonth(nameMonth:String,idYear:String){
        viewModel.setAddMonthDataBase(requireActivity(), MonthEntity(nameMonth,idYear))
        viewModel.getAddMonthDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
                getOrderListMonth()
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }

    /**
     * Elimina mes
     */
    private fun deleteMonth(item:MonthEntity){
        viewModel.setDeleteMonthDataBase(requireActivity(), item)
        viewModel.getDeleteMonthDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                getOrderListMonth()
                dialogMessageTitle(getStringRes(R.string.body_dialog_delete_message_success))
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    getStringRes(R.string.body_dialog_message_data),
                    1
                )
            }
        }
    }

}