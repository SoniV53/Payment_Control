package com.control.paymentcontrol.component.models

data class ColumnsRowModel(
    var numColum:Int,
    var name:String = "",
    var type:String = "row",
    var row:List<RowTableModel> = ArrayList()
)
