package com.control.paymentcontrol.ui.payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.databinding.FragmentFormularyPaymentBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.utils.Status
import android.text.TextWatcher as TextWatcher1

class FormularyPaymentFragment : BaseFragment() {
    private lateinit var binding: FragmentFormularyPaymentBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var monthItem: MonthEntity
    private lateinit var spentItem: SpentEntity
    private var position = -1;
    private var type = 0
    private var isEdit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]

        type = arguments?.getInt(PutArgumentsString.TYPE_ENT)!!
        isEdit = arguments?.getBoolean(PutArgumentsString.TYPE_EDIT)!!
        monthItem = gson.fromJson(arguments?.getString(PutArgumentsString.MONTH_SELECT),MonthEntity::class.java).takeIf { it != null } ?: MonthEntity("","")
        spentItem = gson.fromJson(arguments?.getString(PutArgumentsString.PAYMENT_SELECT),SpentEntity::class.java).takeIf { it != null } ?: SpentEntity("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormularyPaymentBinding.inflate(inflater, container, false)
        showOrHiddenMenuNavbar(false)


        var listMonthInput = requireActivity().resources.getStringArray(R.array.type_spent)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listMonthInput)
        val autoText = (binding.menuType.editText as? AutoCompleteTextView);
        autoText?.setAdapter(adapter)

        binding.cbCom.setOnItemClickListener { p,v,position,id ->
            when(position){
                0 -> visibleQuote(pos = position)
                1 -> visibleQuote(true,pos = position)
                2 -> visibleQuote(true,true,pos = position)
            }
            clearText()
        }

        if (isEdit){
            binding.txtTitle.setText(getStringRes(R.string.menu_update_spent))
            binding.tiTitle.setText(spentItem.title)
            binding.tiDescription.setText(spentItem.description)
            binding.tiAmount.setText(spentItem.amount)
            binding.tiNumberQuotes.setText(spentItem.numberQuota)
            binding.tiQuotesPay.setText(spentItem.quotaPaid)

            binding.chCencel.isChecked = spentItem.cancelPay.equals("1")

            position = if (spentItem.numberQuota.isNotEmpty()) 1 else 0
            autoText?.setText(autoText?.getAdapter()?.getItem(position).toString(), false)
            when(position){
                0 -> visibleQuote(pos = position)
                1 -> visibleQuote(true,pos = position)
                2 -> visibleQuote(true,true,pos = position)
            }
        }else{
            autoText?.setText(autoText?.getAdapter()?.getItem(0).toString(), false)
            visibleQuote(pos = 0)
        }


        binding.btnContinue.setOnClickListener{
           if (position == 0){
               setAddSpent()
           }else{
               var num = if (binding.tiQuotesPay.text.toString().isNotEmpty())binding.tiNumberQuotes.text.toString().toInt() else 0
               if (num <= binding.tiNumberQuotes.text.toString().toInt())
                   setAddSpent()
               else
                   dialogMessageDefault(getStringRes(R.string.error),
                       "CUOTAS PAGADAS NO PUEDE SER MAYOR A NÚMERO CUOTAS",
                       1
                   )
           }

        }
        binding.chCencel.visibility = if(type == 0) View.GONE else View.VISIBLE
        binding.swiFav.visibility = if(type == 0) View.GONE else View.VISIBLE

        textWatcher(binding.tiAmount)
        textWatcher(binding.tiNumberQuotes)
        textWatcher(binding.tiComission)
        textWatcher(binding.tiTitle)
        disableButton()
        return binding.root
    }

    private fun visibleQuote(quote:Boolean = false,details:Boolean = false,pos:Int){
        binding.txtNumberQuotes.visibility = if (!quote) View.GONE else View.VISIBLE
        binding.txtQuotesPay.visibility = if (!quote) View.GONE else View.VISIBLE
        binding.txtCommission.visibility = if (!quote || pos == 1) View.GONE else View.VISIBLE
        binding.constDetails.visibility = if (!details) View.GONE else View.VISIBLE
        binding.mainContra.visibility = View.VISIBLE
        position = pos
        disableButton()
    }

    private fun clearText(){
        binding.tiNumberQuotes.setText("")
        binding.tiQuotesPay.setText("")
    }

    private fun textWatcher(edit:EditText){
        edit.addTextChangedListener {
            disableButton()
        }
    }
    /**
     * ADD NEW Spent
     */
    private fun setAddSpent(){
        var spentData = SpentEntity(
            binding.tiAmount.text.toString(),
            binding.txtDetails.text.toString(),
            binding.tiNumberQuotes.text.toString(),
            binding.tiQuotesPay.text.toString(),
            binding.tiComission.text.toString(),
            binding.tiDescription.text.toString(),
            binding.tiTitle.text.toString(),
            if (binding.chCencel.isChecked && type != 0)"1" else "0",
            idMonth = if (type != 0) monthItem.id.toString() else "",
        )

        if (isEdit)
            spentData.id = spentItem.id

        viewModel.setAddSpentDataBase(requireActivity(), spentData,binding.swiFav.isChecked)
        viewModel.getAddSpentDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS && responseBase.code == Status.CODE_200){
                dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
                requireActivity().onBackPressed()
            }else if(responseBase.status == Status.SUCCESS && responseBase.code == Status.CODE_201){
                dialogMessageDefault(getStringRes(R.string.success),responseBase.message,0)
                requireActivity().onBackPressed()
            }
            else{
                dialogMessageDefault(getStringRes(R.string.error),responseBase.message,1)
            }
        }
    }

    private fun disableButton(){
        when(position){
            0 -> binding.btnContinue.isEnabled = (
                    binding.tiAmount.text.toString().isNotEmpty() && binding.tiTitle.text.toString().isNotEmpty()
                    && ((binding.tiTitle.text?.toString()?.length ?: 0) <= 20))
            1 -> binding.btnContinue.isEnabled = binding.tiAmount.text.toString().isNotEmpty()
                    && binding.tiNumberQuotes.text.toString().isNotEmpty() && binding.tiTitle.text.toString().isNotEmpty()
                    && ((binding.tiTitle.text?.toString()?.length ?: 0) <= 20)
            2 -> binding.btnContinue.isEnabled =  binding.tiAmount.text.toString().isNotEmpty()
                    && binding.tiNumberQuotes.text.toString().isNotEmpty() && binding.tiComission.text.toString().isNotEmpty() && binding.tiTitle.text.toString().isNotEmpty()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        showOrHiddenMenuNavbar(true)
    }
}