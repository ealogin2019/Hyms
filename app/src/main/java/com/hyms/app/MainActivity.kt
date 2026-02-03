package com.hyms.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.hyms.core.ui.HymsTheme
import com.hyms.feature.listing.ui.screen.DraftListingBasicsScreen
import com.hyms.feature.feed.ui.HomeFeedScreen
import com.hyms.feature.feed.ui.ListingDetailScreen // Added import
import com.hyms.feature.listing.ui.screen.SellFlowScreen // Added import for SellFlowScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { HymsAppRoot() }
    }
}

private sealed class Tab(val route: String, val label: String) {
    data object Home : Tab("home", "Home")
    data object Sell : Tab("sell", "Sell")
    data object Inbox : Tab("inbox", "Inbox")
    data object Profile : Tab("profile", "Profile")
}

@Composable
private fun HymsAppRoot() {
    HymsTheme {
        val navController = rememberNavController()
        val tabs = listOf(Tab.Home, Tab.Sell, Tab.Inbox, Tab.Profile)

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val backStack = navController.currentBackStackEntryAsState().value
                    val currentRoute = backStack?.destination?.route

                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(tab.label) },
                            icon = {}
                        )
                    }
                }
            }
        ) {
            padding ->
            NavHost(
                navController = navController,
                startDestination = Tab.Home.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Tab.Home.route) {
                    HomeFeedScreen(navToDetail = { id -> navController.navigate("listing/$id") })
                }
                composable("listing/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: return@composable
                    ListingDetailScreen(id = id)
                }
                composable(Tab.Sell.route) { SellFlowScreen() } // Updated line
                composable(Tab.Inbox.route) { Placeholder("Inbox") }
                composable(Tab.Profile.route) { Placeholder("Profile") }
            }
        }
    }
}

@Composable
private fun Placeholder(title: String) {
    Surface { 
        Text(
            title, 
            style = MaterialTheme.typography.headlineSmall, 
            modifier = Modifier.padding(16.dp)
        ) 
    }
}
