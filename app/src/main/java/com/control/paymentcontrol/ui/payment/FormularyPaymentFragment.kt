package com.control.paymentcontrol.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.databinding.FragmentFormularyPaymentBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel

class FormularyPaymentFragment : BaseFragment() {
    private lateinit var binding: FragmentFormularyPaymentBinding
    private lateinit var viewModel: ServicePaymentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormularyPaymentBinding.inflate(inflater, container, false)


        var listMonthInput = requireActivity().resources.getStringArray(R.array.type_spent)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listMonthInput)
        (binding.menuType.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.cbCom.setOnItemClickListener { p,v,position,id ->
            when(position){
                0 -> visibleQuote()
                1 -> visibleQuote(true)
                2 -> visibleQuote(true,true)
            }
        }

        return binding.root
    }

    private fun visibleQuote(quote:Boolean = false,details:Boolean = false){
        binding.txtNumberQuotes.visibility = if (!quote) View.GONE else View.VISIBLE
        binding.txtQuotesPay.visibility = if (!quote) View.GONE else View.VISIBLE
        binding.txtCommission.visibility = if (!quote) View.GONE else View.VISIBLE
        binding.constDetails.visibility = if (!details) View.GONE else View.VISIBLE
        binding.mainContra.visibility = View.VISIBLE

    }

}