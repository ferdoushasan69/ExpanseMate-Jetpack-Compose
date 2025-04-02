package com.example.expensemate.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemate.R
import com.example.expensemate.ui.theme.bgColor
import com.example.expensemate.ui.theme.primaryColor


@Composable
fun WelComeScreen() {
    Box(modifier = Modifier.fillMaxSize()
        .background(bgColor),
        contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(60.dp))
            Image(
                painterResource(R.drawable.man),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.4f)
            )
            Text(
                "spend smarter \n save more",
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                fontSize = 36.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Button(modifier = Modifier.padding(16.dp).fillMaxWidth().height(67.dp),
                colors = ButtonDefaults.buttonColors(primaryColor),
                onClick = {

                }) {
                Text("Get Started")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun welcomePrev() {
    WelComeScreen()

}