package com.example.expensemate.utils


import com.example.expensemate.R
import com.example.expensemate.data.model.ExpanseEntity
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun formatDateToHumanReadable(timeMillis : Long) : String{
        val simpleDate = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return simpleDate.format(timeMillis)
    }

    fun formatDateForChart(timeMillis : Long) : String{
        val simpleDate = SimpleDateFormat("dd-MM", Locale.getDefault())
        return simpleDate.format(timeMillis)
    }

    fun formatCurrency(amount : Double,locale: Locale = Locale.US) : String{
        val currencyFormatter  = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(amount)
    }
    fun formatDayMonthYear(timeMillis : Long) : String{
        val simpleDate = SimpleDateFormat("MM dd,YYYY", Locale.getDefault())
        return simpleDate.format(timeMillis)
    }

    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }

    fun formatStringDateToMonthDayYear(date: String): String {
        val millis = getMillisFromDate(date)
       return formatDayMonthYear(millis)
    }

    fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
    }

    fun getItemIcon(item: ExpanseEntity): Int {
        return if (item.title == "Paypal") {
            R.drawable.ic_paypal
        } else if (item.title == "Netflix") {
            R.drawable.ic_netflix
        } else if (item.title == "Starbucks") {
            R.drawable.ic_starbucks
        } else {
            R.drawable.ic_upwork
        }
    }

}