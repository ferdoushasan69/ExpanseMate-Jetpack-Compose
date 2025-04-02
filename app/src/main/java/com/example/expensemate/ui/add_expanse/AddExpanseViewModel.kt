package com.example.expensemate.ui.add_expanse

import androidx.lifecycle.viewModelScope

import com.example.expensemate.base.AddExpanseNavigationEvent
import com.example.expensemate.base.BaseViewModel
import com.example.expensemate.base.NavigationEvent
import com.example.expensemate.base.UiEvent
import com.example.expensemate.data.dao.ExpanseDao
import com.example.expensemate.data.model.ExpanseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddExpanseViewModel @Inject constructor(
    private val dao: ExpanseDao
) : BaseViewModel() {

    suspend fun addExpanse(expanseEntity: ExpanseEntity) : Boolean{
        return try {
            dao.insertExpanse(expanseEntity)
            true
        }catch (e : Throwable){
            false
        }
    }
    override fun onEvent(uiEvent: UiEvent) {
        when(uiEvent){
            is ExpanseUiEvent.AddExpanse ->{
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val result = addExpanse(uiEvent.expanseEntity)
                        if (result){
                        _navigationEvent.emit(NavigationEvent.NavigateBack)
                        }
                    }
                }
            }
            is ExpanseUiEvent.OnMenuClick->{
                viewModelScope.launch {
                    _navigationEvent.emit(AddExpanseNavigationEvent.MenuOpenClick)
                }
            }

            is ExpanseUiEvent.OnBackPressed ->{
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateBack)
                }
            }
        }
    }
}