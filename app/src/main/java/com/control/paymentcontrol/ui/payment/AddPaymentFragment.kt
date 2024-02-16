package com.control.paymentcontrol.ui.payment

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.PutArgumentsString.YEAR_SELECT
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status
import java.util.Arrays

class AddPaymentFragment : BaseFragment() {
    private lateinit var binding: FragmentAddPaymentBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var yearItem: YearsEntity
    private lateinit var listMonth: List<MonthEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAddPaymentBinding.inflate(inflater,container,false)

        yearItem = gson.fromJson(arguments?.getString(YEAR_SELECT),YearsEntity::class.java)
        binding.txtTitle.text = yearItem.name

        getOrderListMonth()

        binding.btnContinue.setOnClickListener {
            setAddMonth()
        }

        return binding.root
    }

    /**
     * ADD NEW Month CONTROLLER
     */
    private fun setAddMonth(){
        viewModel.setAddMonthDataBase(requireActivity(), MonthEntity(binding.menuMonth.editText?.text.toString(),yearItem.id.toString()))
        viewModel.getAddMonthDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
                requireActivity().onBackPressed()
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }

    private fun getOrderListMonth(){
        viewModel.fullByMonths(requireActivity(),yearItem.id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                listMonth = responseBase
                var listMonthInput = requireActivity().resources.getStringArray(R.array.month)
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, viewModel.getValidExistMonth(listMonthInput,listMonth))
                (binding.menuMonth.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            }
        }
    }

}