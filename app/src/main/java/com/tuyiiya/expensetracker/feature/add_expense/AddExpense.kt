package com.tuyiiya.expensetracker.feature.add_expense

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tuyiiya.expensetracker.R
import com.tuyiiya.expensetracker.utils.Utils
import com.tuyiiya.expensetracker.data.model.ExpenseEntity
import com.tuyiiya.expensetracker.viewmodel.AddExpenseViewModel
import com.tuyiiya.expensetracker.viewmodel.AddExpenseViewModelFactory
import com.tuyiiya.expensetracker.widget.ExpenseTextView
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController) {
    val context = LocalContext.current
    val factory = remember { AddExpenseViewModelFactory(context) }
    val viewModel: AddExpenseViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                ExpenseTextView(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onAddExpenseClick = {
                    coroutineScope.launch {
                        if (viewModel.addExpense(it)) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun DataForm(modifier: Modifier, onAddExpenseClick: (model: ExpenseEntity) -> Unit) {
    var name = remember {
        mutableStateOf("")
    }
    var amount = remember {
        mutableStateOf("")
    }
    var date = remember {
        mutableLongStateOf(0L)
    }
    var dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("")
    }
    val type = remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ExpenseTextView(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))

        ExpenseTextView(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(8.dp))

        // TODO: Date
        ExpenseTextView(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(date.longValue),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
            ),
            placeholder = { ExpenseTextView(text = "Select date") }
            )
        Spacer(modifier = Modifier.size(8.dp))
        // TODO: Category Dropdown
        ExpenseTextView(text = "Categories", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("Netflix", "Paypal", "Starbucks", "Salary", "Upwork"),
            onItemSelected = {
                category.value = it
            }
        )
        Spacer(modifier = Modifier.size(8.dp))
        // TODO: Type Dropdown
        ExpenseTextView(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("Income", "Expense"),
            onItemSelected = {
                type.value = it
            }
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val model = ExpenseEntity(
                    id = null,
                    title = name.value,
                    amount = amount.value.toDoubleOrNull() ?: 0.0,
                    //date = Utils.formatDateToHumanReadableForm(date.longValue),
                    date = date.longValue,
                    category = category.value,
                    type = type.value
                )
                onAddExpenseClick(model)
            }
        ) {
            ExpenseTextView(
                text = "Add Expense",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(onDateSelected = {
            date.longValue = it
            dateDialogVisibility.value = false
        }, onDismiss = { dateDialogVisibility.value = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DatePicker(state = datePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        ExpenseTextView("Cancel")
                    }

                    TextButton(
                        onClick = {
                            if (selectedDate != null) {
                                onDateSelected(selectedDate)
                            }
                        }
                    ) {
                        ExpenseTextView("OK")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf<String>(listOfItems[0])
    }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it }
    ) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {}) {
            listOfItems.forEach {
                DropdownMenuItem(
                    text = { ExpenseTextView(text = it) },
                    onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false
                    }
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddExpensePreview() {
    AddExpense(rememberNavController())
}