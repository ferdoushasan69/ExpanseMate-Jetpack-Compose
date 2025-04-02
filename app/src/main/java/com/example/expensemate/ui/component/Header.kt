package com.example.expensemate.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemate.R

@Composable
fun Header(
    backIcon: Painter,
    headerTitle: String,
    menuIcon: Painter,
    modifier: Modifier= Modifier,
    backPress : ()-> Unit
) {
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = screenHeight * 0.09f

    Box (modifier){

        Image(
            painterResource(R.drawable.rectangle_9),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                backIcon, contentDescription = null, modifier = Modifier
                    .padding(end = 30.dp)
                    .clickable{
                        backPress()
                    }

            )
            Text(
                headerTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
            )
            Image(
                menuIcon, contentDescription = null,
                modifier = Modifier
                    .padding(start = 30.dp)

            )
        }

    }

}


@Preview(showSystemUi = true)
@Composable
private fun HeaderPreview() {
    Header(
        backIcon = painterResource(R.drawable.ic_back),
        headerTitle = "Add Expanse",
        menuIcon = painterResource(R.drawable.group_8),
        backPress = {},
    )

}