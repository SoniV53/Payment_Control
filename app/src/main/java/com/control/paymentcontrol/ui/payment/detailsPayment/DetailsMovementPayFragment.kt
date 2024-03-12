package com.control.paymentcontrol.ui.payment.detailsPayment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.databinding.FragmentDetailsMovementPayBinding
import com.control.paymentcontrol.ui.base.BaseFragment


class DetailsMovementPayFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailsMovementPayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsMovementPayBinding.inflate(inflater,container,false)

        return binding.root
    }


}