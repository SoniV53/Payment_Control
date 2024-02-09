package com.control.paymentcontrol.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.control.paymentcontrol.database.utils.ConstantsTables.YEAR_TABLE

@Entity(tableName = YEAR_TABLE)
data class YearsEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "amount") var amount: String
)