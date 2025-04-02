package com.example.expensemate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensemate.data.dao.ExpanseDao
import com.example.expensemate.data.model.ExpanseEntity

@Database(entities = [ExpanseEntity::class], version = 2, )
abstract class ExpanseDataBase : RoomDatabase(){
    abstract fun expanseDao(): ExpanseDao
}