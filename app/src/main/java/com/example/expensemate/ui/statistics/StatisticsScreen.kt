package com.example.expensemate.ui.statistics

import android.annotation.SuppressLint
import android.graphics.Path
import android.view.LayoutInflater
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.expensemate.ui.home.TransitionList
import com.example.expensemate.utils.Utils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import androidx.core.graphics.toColorInt
import com.example.expensemate.R
import com.example.expensemate.ui.theme.primaryColor
import kotlin.collections.isNotEmpty
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    viewModel: StatisticViewModel = hiltViewModel()
) {


    val selectTabIndex = remember { mutableIntStateOf(0) }
    val tabItems = listOf("Day", "Week", "Month", "Year")

    val expanseList by viewModel.entries.collectAsState(initial = emptyList())

    val chartEntries = remember(expanseList) {
        viewModel.getEntriesForChart(expanseList)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.icon),
                contentDescription = null
            )
            Text("Statistic", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Image(painterResource(R.drawable.vector__3_), contentDescription = null)
        }
        Spacer(Modifier.height(20.dp))
        TabRow(
            selectedTabIndex = selectTabIndex.intValue,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            divider = {},
            indicator = {}

        ) {
            tabItems.forEachIndexed { index,title->
                Tab(

                    selected = selectTabIndex.intValue == index ,
                    onClick = {selectTabIndex.intValue = index},
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (selectTabIndex.intValue == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .height(40.dp)
                        .background(
                            color = if (selectTabIndex.intValue == index) primaryColor else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )

                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(end = 20.dp, top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.CenterEnd){
            ExpanseIncomeDropDown(
                list = listOf("Expanse","Income")
            ) { }

        }
        val dataState = viewModel.entries.collectAsState(emptyList())
        val topExpense = viewModel.topEntries.collectAsState(initial = emptyList())
        val entries = viewModel.getEntriesForChart(dataState.value)

@Composable
fun LineChart(entries: List<Entry>) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
            view
        }, modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { view ->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, "Expenses").apply {
            color = "#FF2F7E79".toColorInt()
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = "#FF2F7E79".toColorInt()
            val drawable = ContextCompat.getDrawable(context, R.drawable.char_gradient)
            drawable?.let {
                fillDrawable = it
            }

        }

        lineChart.xAxis.valueFormatter =
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong())
                }
            }
        lineChart.data = LineData(dataSet)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpanseIncomeDropDown(
    list : List<String>,
    onSelectedText : (text : String)-> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(list[0]) }
    BoxWithConstraints {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
                .border(1.dp, Color(0xFF666666), RoundedCornerShape(10.dp))
                .clickable { isExpanded = true }
                .padding(horizontal = 8.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedText,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, fontSize = 14.sp) },
                    onClick = {
                        selectedText = item
                        onSelectedText(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun StatisticPreview() {
    StatisticsScreen()

}