package com.tuyiiya.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tuyiiya.expensetracker.data.dao.ExpenseDao
import com.tuyiiya.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        initBasicData(context)
                    }
                }

                private suspend fun initBasicData(context: Context) {
                    val dao = getDatabase(context).expenseDao()

                    dao.insertExpense(
                        ExpenseEntity(
                            1,
                            "Salary",
                            500000.0,
                            System.currentTimeMillis(),
                            "Salary",
                            "Income"
                        )
                    )
                    dao.insertExpense(
                        ExpenseEntity(
                            2,
                            "PayPal",
                            300000.0,
                            System.currentTimeMillis(),
                            "PayPal",
                            "Income"
                        )
                    )
                    dao.insertExpense(
                        ExpenseEntity(
                            3,
                            "Netflix",
                            100000.0,
                            System.currentTimeMillis(),
                            "Netflix",
                            "Expense"
                        )
                    )
                    dao.insertExpense(
                        ExpenseEntity(
                            4,
                            "Starbucks",
                            70000.0,
                            System.currentTimeMillis(),
                            "Starbucks",
                            "Expense"
                        )
                    )
                }
            }).build()
        }
    }
}