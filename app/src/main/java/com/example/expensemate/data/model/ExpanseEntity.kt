package com.example.expensemate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expanse_table")
data class ExpanseEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int?=null,
    val type : String,
    val title : String,
    val amount : Double,
    val date : String,
)
