package com.example.expensemate.ui.statistics


import com.example.expensemate.utils.Utils
import com.example.expensemate.base.BaseViewModel
import com.example.expensemate.base.UiEvent
import com.example.expensemate.data.dao.ExpanseDao
import com.example.expensemate.data.model.ExpanseSummary
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val dao: ExpanseDao
) : BaseViewModel() {

    val entries = dao.getAllExpanseByDate()
    val topEntries = dao.getTopExpanse()
    fun getEntriesForChart(entries: List<ExpanseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in entries) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            println("DEBUG -> date: ${entry.date}, millis: $formattedDate, total: ${entry.total_amount}")

            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
    override fun onEvent(uiEvent: UiEvent) {
    }
}