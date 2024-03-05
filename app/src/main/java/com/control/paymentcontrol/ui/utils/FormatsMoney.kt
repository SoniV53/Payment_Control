package com.control.paymentcontrol.ui.utils

import java.text.DecimalFormat

class FormatsMoney {
    fun formatCurrency(value:String):String{
        val newValue = if (!value.equals("0.0")) value else "0"
        if (newValue.isNotEmpty() && !newValue.equals("0")){
            val de:Double = value.toDouble()
            val formatDe = DecimalFormat("#,###,###,###.00")
            return "GTQ "+formatDe.format(de)
        }
       return "GTQ 0,00"
    }
}