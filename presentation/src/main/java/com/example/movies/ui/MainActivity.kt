package com.example.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movies.common.AppTopBar
import com.example.movies.common.LocalizeApp
import com.example.movies.theme.MoviesTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                val localeState = rememberAppLocale("en")
                LocalizeApp(localeState) { // change this language to ar to se the RTL layout
                    App(onLanguageChange = {
                        localeState.value = Locale(it)
                    })
                }
            }
        }
    }
}

@Composable
fun rememberAppLocale(initialLanguage: String): MutableState<Locale> {
    return remember { mutableStateOf(Locale(initialLanguage)) }
}

@Composable
private fun App(onLanguageChange: (lang: String) -> Unit) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    Scaffold(
        topBar = {
            AppTopBar(
                showBackButton = currentDestination == "com.example.movies.ui.MediaDetailRoute/{media}",
                onLanguageChange = onLanguageChange,
                navController = navController
            )
        }
    ) { padding ->
        MainGraph(padding, navController)
    }
}