package com.control.roomdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.control.roomdatabase.utils.ConstantsNames.MONTH_TABLE
@Entity(tableName = MONTH_TABLE, foreignKeys = [ForeignKey(
    entity = YearsEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("idYear"),
    onDelete = ForeignKey.CASCADE
)])
data class MonthEntity (
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "idYear") val idYear: String,
    @ColumnInfo(name = "total") var total: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
)
