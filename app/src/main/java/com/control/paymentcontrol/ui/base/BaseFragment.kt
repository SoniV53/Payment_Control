package com.control.paymentcontrol.ui.base

import androidx.fragment.app.Fragment
import com.control.paymentcontrol.ui.utils.InterfaceNavBar
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBar

open class BaseFragment : Fragment() {
    private lateinit var interfaceNavBar: InterfaceNavBar

    fun hiddenNavbar(){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.hiddenNavBar()
        }
    }

    fun showNavbar(){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.hiddenNavBar()
        }
    }

    fun onClickMoreNavbar(action: OnActionButtonNavBar){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.onClickMore(action)
        }
    }
}