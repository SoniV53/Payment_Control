package com.control.paymentcontrol.ui.payment.detailsPayment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.component.bottomSheet.BottomAddBalance
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.databinding.FragmentDetailsMovementPayBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.ui.utils.OnClickInterfaceValue
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status
import java.util.Arrays


class DetailsMovementPayFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailsMovementPayBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var yearItem: YearsEntity
    private lateinit var monthItem: MonthEntity
    private lateinit var adapterPay: AdapterDataPayment
    private lateinit var spentList: List<SpentEntity>
    private var payMonthTotal = 0.00
    private var calcPayMonthTotal = 0.00
    private var totalMonth = ""
    private var isDetails = false
    private lateinit var controllerMov:ControllerDataMovements
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
        yearItem = gson.fromJson(arguments?.getString(PutArgumentsString.YEAR_SELECT), YearsEntity::class.java)
        monthItem = gson.fromJson(arguments?.getString(PutArgumentsString.MONTH_SELECT),MonthEntity::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsMovementPayBinding.inflate(inflater,container,false)
        onClickMoreNavbar(object: OnActionButtonNavBarMenu {
            override fun onActionPositionTwo() {
                isDetails = false
                val bundle = Bundle()
                bundle.putString(PutArgumentsString.MONTH_SELECT,gson.toJson(monthItem))
                bundle.putInt(PutArgumentsString.TYPE_ENT,1)
                findNavController().navigate(R.id.action_detailsMovementPayFragment_to_formularyPaymentFragment,bundle)
            }

            override fun onActionPositionOne() {
                isDetails = false
                val bundle = Bundle()
                bundle.putInt(PutArgumentsString.TYPE_ENT,1)
                bundle.putString(PutArgumentsString.ID_MONTH,monthItem.id.toString())
                bundle.putBoolean(PutArgumentsString.TYPE_EDIT,false)
                findNavController().navigate(R.id.action_detailsMovementPayFragment_to_paymentFavoritesFragment,bundle)
            }

            override var attr: List<AttributesDesign>
                get() = Arrays.asList(
                    AttributesDesign(1,getStringRes(R.string.favorites_add),R.drawable.star_solid),
                    AttributesDesign(2, getStringRes(R.string.menu_add_spent),R.drawable.comment_dollar_solid)
                )
                set(value) {}
        })
        init()
        onActionClick()

        return binding.root
    }

    private fun  init(){
        binding.txtBalance.text = format.formatCurrency(monthItem?.total.toString())
        binding.movementCalendar.getBindingCalendar().btnContinue.visibility = View.GONE
        controllerMov = ControllerDataMovements(
            binding.movementCalendar,
            monthItem,
            viewModel,
            requireActivity(),
            this,
            this,
            idNav = R.id.action_detailsMovementPayFragment_to_formularyPaymentFragment)

        controllerMov.getOrderSpent()
    }

    private fun onActionClick(){
        binding.mainPresu.setOnClickListener{
            val modalBottomSheet = BottomAddBalance(monthItem.total,object: OnClickInterfaceValue {
                override fun onClickAction(value: String) {
                    setUpdateMonth(value,true)
                }
            })
            modalBottomSheet.show(requireActivity().supportFragmentManager, "")
        }
    }

    /**
     * Update Month CONTROLLER
     */
    private fun setUpdateMonth(value:String = "",isMessage:Boolean = false){
        controllerMov.calcDetails()
        val updateMonth = monthItem
        updateMonth.total = value.ifEmpty { monthItem.total }
        updateMonth.payTotalMonth = payMonthTotal.toString()
        viewModel.setUpdateMonthDataBase(requireActivity(), updateMonth).observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                if (isMessage)
                    dialogMessageTitle("SE ACTUALIZO")
                init()
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }


}