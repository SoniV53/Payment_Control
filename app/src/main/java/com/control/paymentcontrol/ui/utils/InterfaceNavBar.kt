package com.control.paymentcontrol.ui.utils

import com.control.paymentcontrol.models.AttributesDesign

interface InterfaceNavBar {
    fun showOrHiddenNavbar(boolean: Boolean)
    fun showOrHiddenMenuNavbar(boolean: Boolean)
    fun onClickMore(action:OnActionButtonNavBarMenu)
}