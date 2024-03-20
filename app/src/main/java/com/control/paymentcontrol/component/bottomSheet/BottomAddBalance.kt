package com.control.paymentcontrol.component.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.BottomAddBalanceBinding
import com.control.paymentcontrol.ui.utils.OnClickInterfaceValue
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomAddBalance(var amount:String,var listener:OnClickInterfaceValue) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomAddBalanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomAddBalanceBinding.inflate(inflater,container,false)

        binding.tiAmount.setText(amount)
        binding.btnContinue.setOnClickListener{
            var input = binding.tiAmount.text.toString().trim()
            if (!amount.equals(input))
                listener.onClickAction(input)
            dismiss()
        }

        return binding.root
    }



    companion object {
        const val TAG = "ModalBottomSheet"
    }

}