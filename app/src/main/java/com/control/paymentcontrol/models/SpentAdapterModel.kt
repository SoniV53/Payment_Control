package com.control.paymentcontrol.models

import com.control.roomdatabase.entities.SpentEntity

data class SpentAdapterModel(
    val spentEntity: SpentEntity,
    val type: String = "m"
)
