package com.example.expensemate.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensemate.data.model.ExpanseEntity
import com.example.expensemate.data.model.ExpanseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpanseDao {


    @Query("SELECT * FROM expanse_table")
    fun getAllExpanse() : Flow<List<ExpanseEntity>>
    @Query("SELECT * FROM expanse_table WHERE type = 'Expense' ORDER BY amount DESC LIMIT 5")
    fun getTopExpanse(): Flow<List<ExpanseEntity>>


    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expanse_table where type = :type GROUP BY type, date ORDER BY date")
    fun getAllExpanseByDate(type: String = "Expense"): Flow<List<ExpanseSummary>>

    @Insert
    suspend fun insertExpanse(expanseEntity: ExpanseEntity)

    @Update
    suspend fun updateExpanse(expanseEntity: ExpanseEntity)

    @Delete
    suspend fun deleteExpanse(expanseEntity: ExpanseEntity)

}