package com.control.roomdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.control.roomdatabase.utils.ConstantsNames

@Entity(tableName = ConstantsNames.SPENT_TABLE)
data class SpentEntity (
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "amountQuota") var amountQuota: String = "",
    @ColumnInfo(name = "numberQuota") var numberQuota: String = "",
    @ColumnInfo(name = "quotaPaid") var quotaPaid: String = "",
    @ColumnInfo(name = "commission") var commission: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "cancelPay") var cancelPay: String = "",
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    @ColumnInfo(name = "idMonth") var idMonth: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
)