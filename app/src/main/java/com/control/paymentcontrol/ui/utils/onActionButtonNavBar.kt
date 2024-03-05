package com.control.paymentcontrol.ui.utils

import com.control.paymentcontrol.models.AttributesDesign

interface OnActionButtonNavBarMenu {
    fun onActionPositionOne()
    fun onActionPositionTwo(){}

    var attr:List<AttributesDesign>
}