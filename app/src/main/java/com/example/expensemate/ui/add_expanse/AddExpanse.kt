package com.example.expensemate.ui.add_expanse

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemate.ui.component.Header

import com.example.expensemate.utils.Utils
import com.example.expensemate.R
import com.example.expensemate.data.model.ExpanseEntity
import com.example.expensemate.ui.theme.primaryColor
import com.example.expensemate.ui.theme.textColor
import kotlinx.coroutines.launch

@Composable
fun AddExpanse(
    expanseViewModel: AddExpanseViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topMargin = screenHeight * 0.13f // 20% of screen height
    val scope = rememberCoroutineScope()
    Column {
        ConstraintLayout {
            val (header, card) = createRefs()
            Header(
                backIcon = painterResource(R.drawable.ic_back),
                headerTitle = "Add Expanse",
                menuIcon = painterResource(R.drawable.group_8),
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                backPress = {
                    navHostController.navigateUp()
                }
            )
            ExpanseCard(
                modifier = Modifier
                    .constrainAs(card) {
                        top.linkTo(header.bottom, -topMargin)
                    },
                addExpanseClick = {
                    scope.launch {
                        expanseViewModel.addExpanse(it)
                    }
                },
                navHostController = navHostController
            )
        }

    }

}

@Composable
fun ExpanseCard(
    modifier: Modifier = Modifier,
    addExpanseClick: (model: ExpanseEntity) -> Unit,
    navHostController: NavHostController
) {
    var dialogVisibility by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var types by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(0L) }
    var isDropDownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)

            .shadow(1.dp, shape = RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        CustomTextField(
            text = "NAME",
            onValueChange = {
                name = it
            },
            valueText = name,


            )

        CustomTextField(
            text = "AMOUNT",
            onValueChange = {
                amount = it
            },
            valueText = amount,

            )

        SingleText("TYPE")
        ExpanseDropDownMenu(
            onItemSelected = {
                types = it
            },
            list = listOf("Income", "Expanse"),
            onDismiss = {}
        )
        SingleText("CATEGORY")
        ExpanseDropDownMenu(
            onItemSelected = {
                category = it
            },
            list = listOf("Netflix", "Paypal", "Starbucks", "Salary", "Upwork"),
            onDismiss = {
                isDropDownExpanded = false
            }
        )
        SingleText("DATE")
        OutlinedTextField(
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            onValueChange = {
            },
            value =if (date == 0L) "00/00/00" else Utils.formatDateToHumanReadable(date),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dialogVisibility = true
                }
                .border(width = 1.dp, color = Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledIndicatorColor = Color.Transparent, // Remove default disabled outline color

                disabledContainerColor = MaterialTheme.colorScheme.background, // Keep background white when disabled
                disabledTextColor = textColor, // Ensure text is visible when disabled
            ),
            enabled = false,

            )
        Spacer(Modifier.height(17.dp))
        Button(
            onClick = {
                val model = ExpanseEntity(
                    id = null,
                    type = types,
                    title = name,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    date = Utils.formatDateToHumanReadable(date)
                )
                if ( name.isEmpty() || amount.isEmpty() ){
                    Toast.makeText(context,"Please Field All fields", Toast.LENGTH_SHORT).show()
                }else{
                addExpanseClick(model)
                navHostController.navigateUp()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Add Expanse", color = MaterialTheme.colorScheme.onBackground)
        }
    }
    if (dialogVisibility) {
        ExpanseDatePickerDialog(
            onSelected = {
                date = it
                dialogVisibility = false
            },
            onDismiss = {
                dialogVisibility = false
            }
        )
    }
}

@Composable
fun SingleText(text: String) {
    Spacer(Modifier.height(8.dp))
    Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    Spacer(Modifier.height(10.dp))


}

@Composable
fun CustomTextField(
    text: String,
    trailIcon: ImageVector? = null,
    onValueChange: (String) -> Unit,
    valueText: String,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            trailingIcon = {
                Icon(trailIcon ?: Icons.Default.ArrowDropDown, contentDescription = null)
            },
            onValueChange = {
                onValueChange(it)
            }, value = valueText,
            modifier = modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            )
        )
        Spacer(Modifier.height(10.dp))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpanseDropDownMenu(
    onItemSelected: (item: String) -> Unit,
    list: List<String>,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(list[0]) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
    ) {
        OutlinedTextField(
            readOnly = true,

            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            onValueChange = {
            }, value = selectedText,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .border(width = 1.dp, color = Color(0xFFDDDDDD), shape = RoundedCornerShape(10.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onDismiss()
            },
            modifier = Modifier.background(Color.White)
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedText = item
                        onItemSelected(item)
                        expanded = false
                    },

                    )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpanseDatePickerDialog(
    onSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L

    DatePickerDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                onSelected(selectedDate)
                onDismiss()
            }) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = {
                onSelected(selectedDate)
            }) { Text("Confirm") }
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }

}
//
//@Preview(showSystemUi = true)
//@Composable
//private fun AddExpansePreview() {
//    AddExpanse()
//}