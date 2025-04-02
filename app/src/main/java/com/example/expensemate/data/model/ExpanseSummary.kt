package com.example.expensemate.data.model

import androidx.room.Entity

@Entity
data class ExpanseSummary(
    val type : String,
    val date : String,
    val total_amount : Double
)
