package com.control.paymentcontrol.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.databinding.FragmentSelectDestineBinding
import com.control.paymentcontrol.ui.home.AddYearFragment
import com.control.paymentcontrol.ui.home.HomeFragment


class SelectDestineFragment : BaseFragment() {

    private lateinit var binding: FragmentSelectDestineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectDestineBinding.inflate(inflater,container,false)
        showOrHiddenNavbar(false)

        var suppor = requireActivity().supportFragmentManager


        if ( getPreferenceCache() != null){
            suppor.commit {
                setReorderingAllowed(true)
                add(R.id.frameMain, HomeFragment())
            }
        }else{
            suppor.commit {
                setReorderingAllowed(true)
                add(R.id.frameMain, AddYearFragment())
            }
        }

        return binding.root
    }


}