package com.control.roomdatabase.entities.embeddedWith

import androidx.room.Embedded
import androidx.room.Relation
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity

data class MonthWithSpent(
    @Embedded val month: MonthEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idMonth"
    )
    val spent: List<SpentEntity>
)
