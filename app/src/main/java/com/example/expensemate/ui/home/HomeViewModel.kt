package com.example.expensemate.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensemate.utils.Utils
import com.example.expensemate.base.BaseViewModel
import com.example.expensemate.base.HomeNavigationEvent
import com.example.expensemate.base.UiEvent
import com.example.expensemate.data.dao.ExpanseDao
import com.example.expensemate.data.model.ExpanseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: ExpanseDao
) : BaseViewModel(){
    val expanse = dao.getAllExpanse()

    private val _uiState : MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val uiState : StateFlow<HomeState> = _uiState.asStateFlow()

    private val _uiLiveDate : MutableLiveData<HomeState> = MutableLiveData(HomeState())
    val uiLiveDate : LiveData<HomeState> = _uiState.asLiveData()

    fun deleteExpanse(expanseEntity: ExpanseEntity){
        viewModelScope.launch {
            dao.deleteExpanse(expanseEntity)
        }
    }

    override fun onEvent(uiEvent: UiEvent) {

        when(uiEvent) {
            is HomeEvent.OnAddExpanseClick -> {
                viewModelScope.launch {
                    _navigationEvent.emit(HomeNavigationEvent.NavigationAddExpanse)
                }
            }

            is HomeEvent.OnSeeAllClick -> {
                viewModelScope.launch {
                _navigationEvent.emit(HomeNavigationEvent.NavigateSeeAllExpanse)
                }
            }

            is HomeEvent.OnIncomeExpanseClick -> {
                viewModelScope.launch {
                    _navigationEvent.emit(HomeNavigationEvent.NavigationIncomeExpanse)

                }
            }
        }
    }

    fun getBalance(list : List<ExpanseEntity>) : String{
        var balance = 0.0
        for (expanse in list){
            if (expanse.title == "Income"){
                balance += expanse.amount
            }else{
                balance -= expanse.amount
            }
        }
        return Utils.formatToDecimalValue(balance)
    }

    fun getTotalExpanse(list : List<ExpanseEntity>): String{
        var totalExpanse = 0.0
        for (expanse in list){
            if (expanse.type != "Income"){
                totalExpanse += expanse.amount
            }
        }
        return Utils.formatCurrency(totalExpanse)
    }

    fun getTotalIncome(list: List<ExpanseEntity>): String{
        var totalIncome = 0.0
        for (income in list){
            if (income.type == "Income"){
                totalIncome += income.amount
            }
        }
        return Utils.formatCurrency(totalIncome)
    }

}