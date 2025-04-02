package com.example.expensemate.di

import android.content.Context
import androidx.room.Room
import com.example.expensemate.data.dao.ExpanseDao
import com.example.expensemate.data.database.ExpanseDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {



    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ExpanseDataBase {
        return Room.databaseBuilder(
            context,
            ExpanseDataBase::class.java,
            "expanse_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideExpanseDao(expanseDataBase: ExpanseDataBase) : ExpanseDao{
        return expanseDataBase.expanseDao()
    }
}