package com.example.expensemate.ui.add_expanse

import com.example.expensemate.base.UiEvent
import com.example.expensemate.data.model.ExpanseEntity

sealed class ExpanseUiEvent : UiEvent(){
    data class AddExpanse(val expanseEntity: ExpanseEntity) : ExpanseUiEvent()
    data object OnBackPressed: ExpanseUiEvent()
    data object OnMenuClick: ExpanseUiEvent()
}