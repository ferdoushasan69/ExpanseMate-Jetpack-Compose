package com.example.expensemate.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.navigation.AddExpanse
import com.example.expansetracker.navigation.Equalizer
import com.example.expansetracker.navigation.Home
import com.example.expansetracker.navigation.User
import com.example.expansetracker.navigation.Wallet
import com.example.expensemate.ui.add_expanse.AddExpanse
import com.example.expensemate.ui.home.HomeScreen
import com.example.expensemate.ui.statistics.StatisticsScreen
import com.example.expensemate.R
import com.example.expensemate.ui.theme.navBarIconSelectedColor
import com.example.expensemate.ui.theme.navBarIconUnSelectedColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val list = listOf(
        TabItem(
            title = "Home",
            icon = painterResource(R.drawable.vector)
        ),
        TabItem(
            title = "Equalizer",
            icon = painterResource(R.drawable.vector__1_)
        ),
        TabItem(
            title = "Add",
            icon = painterResource(R.drawable.add_outline_1)
        ),
        TabItem(
            title = "Wallet",
            icon = painterResource(R.drawable.vector__2_)
        ),
        TabItem(
            title = "User",
            icon = painterResource(R.drawable.user__1__1)
        ),
    )
    val screens = listOf(
        Home, Equalizer, AddExpanse, Wallet, User
    )
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val backStackEntry = navController.currentBackStackEntryFlow.collectAsState(null)
    val currentState = backStackEntry.value?.destination?.route
    val navBarState = screens.indexOfFirst { it::class.qualifiedName == currentState }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AnimatedVisibility(visible = isBottomBarVisible) {

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background, // Bottom bar background
                    tonalElevation = 0.dp
                ) {
                    list.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = navBarIconSelectedColor,
                                unselectedIconColor = MaterialTheme.colorScheme.background,
                                indicatorColor = Color.Transparent // Removes background color
                            ),
                            interactionSource = MutableInteractionSource(),
                            selected = navBarState == index,
                            onClick = {
                                navController.navigate(screens[index]){
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true // Prevent multiple copies
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = item.icon, contentDescription = item.title,
                                    tint =
                                        if (navBarState == index) navBarIconSelectedColor else navBarIconUnSelectedColor
                                )
                            },
                            label = {
                                Text(item.title, color = MaterialTheme.colorScheme.onBackground)
                            }

                            )
                    }
                }
            }
        }
    ) {

        NavHost(navController = navController, startDestination = Home,
            enterTransition = { slideIn(
                initialOffset = { fullSize -> IntOffset(fullSize.width, 0) },
                animationSpec = tween(400) // Optional: You can customize the duration
            ) },
            exitTransition = {   slideOut(
                targetOffset = { fullSize -> IntOffset(-fullSize.width, 0) },
                animationSpec = tween(400) // Optional: Customize exit duration
            ) }
            ) {

            composable<Home> {
                isBottomBarVisible = true
                HomeScreen(navController)
            }

            composable<Equalizer> {
                isBottomBarVisible = true

                StatisticsScreen()
            }
            composable<AddExpanse> {
                isBottomBarVisible = true
                AddExpanse(navHostController = navController)
            }

            composable<Wallet> {
                isBottomBarVisible = true

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Wallet")
                }
            }
            composable<User> {
                isBottomBarVisible = true

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("User")
                }
            }
        }
    }

}

data class TabItem(
    val title: String,
    val icon: Painter
)