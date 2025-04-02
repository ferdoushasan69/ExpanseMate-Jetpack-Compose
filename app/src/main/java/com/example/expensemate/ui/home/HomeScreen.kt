package com.example.expensemate.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemate.utils.Utils
import com.example.expensemate.R
import com.example.expensemate.data.model.ExpanseEntity
import com.example.expensemate.ui.theme.cardColor
import com.example.expensemate.ui.theme.negativeAmountColor
import com.example.expensemate.ui.theme.positiveAmountColor

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()

) {
    val state = viewModel.expanse.collectAsState(initial = emptyList())
    val income = viewModel.getTotalIncome(state.value)
    val expanse = viewModel.getTotalExpanse(state.value)
    val balance = viewModel.getBalance(state.value)
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ExpanseEntity?>(null) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteExpanse(selectedItem!!)
                    showDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                val (headerImg, tabRaw, card, addIcon) = createRefs()
                Image(
                    painterResource(R.drawable.rectangle_9),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(headerImg) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    contentScale = ContentScale.Crop
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(tabRaw) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(card.top)
                        }
                        .padding(top = 60.dp, start = 10.dp, end = 10.dp)
                ) {
                    Column {
                        Text(
                            "Good morning",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            "Mohammad",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Image(
                        painterResource(R.drawable.notification),
                        contentDescription = null,
                        modifier = Modifier.size(38.dp)
                    )
                }
                CardItem(
                    modifier = Modifier.constrainAs(card) {
                        top.linkTo(headerImg.bottom)
                        bottom.linkTo(headerImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    income = income,
                    expanse = expanse,
                    balance = balance
                )


            }
            TransitionList(
                modifier = Modifier.padding(bottom = 50.dp),
                list = state.value,

                onItemDelete = {item->
                    selectedItem = item
                    showDialog = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitionList(
    modifier: Modifier = Modifier,
    transitionText: String = "Transactions history",
    list : List<ExpanseEntity>,
    onItemDelete : (model : ExpanseEntity) -> Unit
) {


    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                transitionText, fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
            if (transitionText == "Transactions history") {
                Text(
                    "See All", fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }
        }
        LazyColumn(modifier = modifier) {
            items(list){item->
            val icon = Utils.getItemIcon(item)
                val amount  = if (item.type == "Income") item.amount else item.amount * -1
                val color = if (item.type =="Income") positiveAmountColor else negativeAmountColor
                ListItem(
                    icon = painterResource(icon),
                    type = item.title,
                    date = Utils.formatStringDateToMonthDayYear(item.date),
                    amount = Utils.formatCurrency(amount),
                    onItemClick = {
                       onItemDelete(item)
                    },
                    color = color
                )
            }
        }
    }

}

@Composable
fun ListItem(
    icon: Painter,
    type: String,
    date: String,
    amount: String,
    onItemClick: () -> Unit,
    color: Color,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onItemClick()
                    }
                )
            }
           ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(icon, contentDescription = null)
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(type, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(7.dp))
                Text(date, fontSize = 13.sp, fontWeight = FontWeight.Normal)
            }
        }
        Text(amount, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = color)

    }

}

@Composable
fun CardItem(modifier: Modifier = Modifier, income: String, expanse: String, balance: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(201.dp)
            .shadow(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (header, totalBalance, threeDot) = createRefs()
            val chain =
                createHorizontalChain(header, threeDot, chainStyle = ChainStyle.SpreadInside)
            Row(modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }) {
                Text(
                    "Total Balance",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Image(
                    painterResource(R.drawable.chevron_down),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
            Text(
                balance, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.White,
                modifier = Modifier.constrainAs(totalBalance) {
                    top.linkTo(header.bottom, margin = 8.dp)
                    start.linkTo(header.start)
                })
            Image(
                painterResource(R.drawable.group_8),
                contentDescription = null,
                modifier = Modifier.constrainAs(threeDot) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, 8.dp)
                })

        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            CardItemText(
                incomeTypes = "Income",
                balanceTypes = income,
                img = painterResource(R.drawable.arrow_down_1)
            )
            CardItemText(
                incomeTypes = "Expanse",
                balanceTypes = expanse,
                img = painterResource(R.drawable.arrow_down_1__1_)
            )

        }


    }
}

@Composable
fun CardItemText(incomeTypes: String, balanceTypes: String, img: Painter) {
    ConstraintLayout(modifier = Modifier) {
        val (arrowImg,  balance) = createRefs()
        Row(modifier = Modifier.constrainAs(arrowImg) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }) {
            Image(img, contentDescription = null, modifier = Modifier)
            Text(
                incomeTypes,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Color(0xFFD0E5E4)
            )
        }
        Text(
            balanceTypes,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.constrainAs(balance) {
                top.linkTo(arrowImg.bottom, 8.dp)
                start.linkTo(arrowImg.start)
            })
    }
}

