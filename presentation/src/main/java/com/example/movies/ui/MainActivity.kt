package com.example.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies.theme.MoviesTheme
import com.example.movies.ui.search.MediaDetailsPage
import com.example.movies.ui.search.SearchMediaPage
import com.example.movies.ui.search.SearchMediaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                App()
            }
        }
    }

    @Composable
    private fun App() {
        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = "search_destination"
            ) {
                composable("search_destination") {
                    SearchMediaPage(navController)
                }
                composable("detail_destination") {
                    MediaDetailsPage(navController)
                }

            }
        }
    }
}
