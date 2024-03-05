package com.control.roomdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.control.roomdatabase.utils.ConstantsNames.YEAR_TABLE

@Entity(tableName = YEAR_TABLE)
data class YearsEntity (
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "amount") var amount: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
)
