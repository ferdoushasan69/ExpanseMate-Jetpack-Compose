package com.example.expensemate.base

sealed class NavigationEvent {

    data object NavigateBack : NavigationEvent()

}

sealed class AddExpanseNavigationEvent : NavigationEvent() {

    data object MenuOpenClick : AddExpanseNavigationEvent()

}

sealed class HomeNavigationEvent : NavigationEvent() {
    data object NavigationAddExpanse : HomeNavigationEvent()
    data object NavigationIncomeExpanse : HomeNavigationEvent()
    data object NavigateSeeAllExpanse : HomeNavigationEvent()
}