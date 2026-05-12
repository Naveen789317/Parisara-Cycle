package com.parisara.cycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.parisara.cycle.ui.screens.*
import com.parisara.cycle.ui.theme.ParisaraCycleTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parisara.cycle.data.local.AppDatabase
import com.parisara.cycle.data.local.SessionManager
import com.parisara.cycle.data.repository.UserRepository
import com.parisara.cycle.ui.viewmodels.AuthViewModel
import com.parisara.cycle.ui.viewmodels.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParisaraCycleTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Routes : Screen("routes", "Routes", Icons.Filled.Map)
    object Stats : Screen("stats", "Stats", Icons.Filled.BarChart)
    object Buddy : Screen("buddy", "Buddy", Icons.Filled.Group)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

sealed class AuthScreen(val route: String) {
    object Splash : AuthScreen("splash")
    object Welcome : AuthScreen("welcome")
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val sessionManager = SessionManager(context)
    val repository = UserRepository(database.userDao())
    val factory = AuthViewModelFactory(repository, sessionManager)
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Routes,
        Screen.Stats,
        Screen.Buddy,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val isAuthRoute = currentRoute in listOf(
        AuthScreen.Splash.route,
        AuthScreen.Welcome.route,
        AuthScreen.Login.route,
        AuthScreen.Register.route
    )

    Scaffold(
        bottomBar = {
            if (!isAuthRoute) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AuthScreen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AuthScreen.Splash.route) {
                SplashScreen(
                    authViewModel = authViewModel,
                    onNavigateToWelcome = {
                        navController.navigate(AuthScreen.Welcome.route) {
                            popUpTo(AuthScreen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(AuthScreen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(AuthScreen.Welcome.route) {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate(AuthScreen.Login.route) },
                    onNavigateToRegister = { navController.navigate(AuthScreen.Register.route) }
                )
            }
            composable(AuthScreen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(AuthScreen.Welcome.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AuthScreen.Register.route) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(AuthScreen.Welcome.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Routes.route) { MapScreen(navController) }
            composable(Screen.Stats.route) { EcoStatsScreen() }
            composable(Screen.Buddy.route) { BuddySystemScreen() }
            composable(Screen.Profile.route) { ProfileScreen(authViewModel = authViewModel, onLogout = {
                navController.navigate(AuthScreen.Welcome.route) {
                    popUpTo(0)
                }
            }) }
        }
    }
}
