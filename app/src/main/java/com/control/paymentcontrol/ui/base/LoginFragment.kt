package com.control.paymentcontrol.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.control.paymentcontrol.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        //hiddenNavbar()

        return binding.root
    }

}