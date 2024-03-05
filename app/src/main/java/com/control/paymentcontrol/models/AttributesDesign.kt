package com.control.paymentcontrol.models

import com.control.paymentcontrol.R

data class AttributesDesign (
    var position:Int,
    var title:String="",
    var icon:Int= R.drawable.info_circle_solid,
    var visible:Boolean= true,

)