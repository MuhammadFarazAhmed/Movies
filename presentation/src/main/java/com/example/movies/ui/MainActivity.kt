package com.example.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies.theme.MoviesTheme
import com.example.movies.ui.search.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->

                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = rememberNavController(),
                        startDestination = "search_destination"
                    ) {
                        composable("search_destination") {
                            SearchScreen()
                        }

                    }
                }
            }
        }
    }
}
