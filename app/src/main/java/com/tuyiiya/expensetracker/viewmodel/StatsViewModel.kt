package com.tuyiiya.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.tuyiiya.expensetracker.data.ExpenseDatabase
import com.tuyiiya.expensetracker.data.dao.ExpenseDao
import com.tuyiiya.expensetracker.data.model.ExpenseSummary
import com.tuyiiya.expensetracker.utils.Utils

class StatsViewModel(dao: ExpenseDao): ViewModel() {
    val entries = dao.getAllExpenseByDate()
    val topEntries = dao.getTopExpenses()

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        if (entries.isEmpty()) return emptyList()

        val list = mutableListOf<Entry>()

        val minDate = entries.minOf { Utils.getMillisFromDate(it.date) }

        for ((index, entry) in entries.withIndex()) {
            val formattedDate = Utils.getMillisFromDate(entry.date)

            // Normalize X by subtracting minDate, OR just use index
            val normalizedX = (formattedDate - minDate).toFloat()

            //list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
            list.add(Entry(normalizedX, entry.total_amount.toFloat()))
        }
        return list
    }
}


class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()

            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}