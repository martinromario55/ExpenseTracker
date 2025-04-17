package com.tuyiiya.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tuyiiya.expensetracker.data.ExpenseDatabase
import com.tuyiiya.expensetracker.data.dao.ExpenseDao
import com.tuyiiya.expensetracker.data.model.ExpenseEntity
import com.tuyiiya.expensetracker.R

class HomeViewModel(dao: ExpenseDao): ViewModel() {
    val expenses = dao.getAllExpenses()

    fun getBalance(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            } else {
                total -= it.amount
            }
        }
        return "UGX $total"
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "UGX $total"
    }

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "UGX $total"
    }

    fun getItemIcon(item: ExpenseEntity): Int {
        if (item.category == "Salary") {
            return R.drawable.ic_upwork
        } else if (item.category == "PayPal") {
            return R.drawable.ic_paypal
        } else if (item.category == "Netflix") {
            return R.drawable.ic_netflix
        } else if (item.category == "Starbucks") {
            return R.drawable.ic_starbucks
        } else {
            return R.drawable.ic_upwork
        }
    }
}


class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}