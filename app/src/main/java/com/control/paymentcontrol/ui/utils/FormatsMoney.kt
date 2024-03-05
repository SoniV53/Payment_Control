package com.control.paymentcontrol.ui.utils

import java.text.DecimalFormat

class FormatsMoney {
    fun formatCurrency(value:String):String{
        if (value.isNotEmpty()){
            val de:Double = value.toDouble()
            val formatDe = DecimalFormat("#,###,###,###.00")
            return "GTQ "+formatDe.format(de)
        }
       return "0"
    }
}