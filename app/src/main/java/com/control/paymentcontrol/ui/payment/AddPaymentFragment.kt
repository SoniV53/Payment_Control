package com.control.paymentcontrol.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.ui.base.BaseFragment
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAddPaymentBinding.inflate(inflater,container,false)

        yearItem = gson.fromJson(arguments?.getString(YEAR_SELECT),YearsEntity::class.java)

        binding.txtTitle.text = yearItem.name

        val listMonth = requireActivity().resources.getStringArray(R.array.month)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listMonth)
        (binding.menuMonth.editText as? AutoCompleteTextView)?.setAdapter(adapter)

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
}