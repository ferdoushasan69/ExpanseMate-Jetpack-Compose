package com.example.expensemate.ui.home

import com.example.expensemate.base.UiEvent

sealed class HomeEvent : UiEvent(){
    data object OnAddExpanseClick : HomeEvent()
    data object OnIncomeExpanseClick : HomeEvent()
    data object OnSeeAllClick : HomeEvent()
}