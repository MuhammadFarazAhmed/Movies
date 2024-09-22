package com.example.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.domain.model.Media
import com.example.movies.common.CustomNavType
import com.example.movies.common.LocalizeApp
import com.example.movies.theme.MoviesTheme
import com.example.movies.ui.search.MediaDetailsPage
import com.example.movies.ui.search.SearchMediaPage
import com.example.movies.vm.SearchMoviesViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                var language by remember { mutableStateOf("") }
                LocalizeApp(language) { // change this language to ar to se the RTL layout
                    App {
                        language = it
                    }
                }
            }
        }
    }
}

@Serializable
data object SearchMediaRoute

@Serializable
data class MediaDetailRoute(val media: Media)

@Composable
private fun App(onLanguageChange: (lang: String) -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(onLanguageChange, navController)
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = SearchMediaRoute
        ) {
            composable<SearchMediaRoute> {
                val vm: SearchMoviesViewModel = koinViewModel()
                SearchMediaPage(vm = vm) { media ->
                    navController.navigate(MediaDetailRoute(media))
                }
            }
            composable<MediaDetailRoute>(
                typeMap = mapOf(
                    typeOf<Media>() to CustomNavType.MediaType
                )
            ) {
                val arguments = it.toRoute<MediaDetailRoute>()
                MediaDetailsPage(arguments.media, navController)
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppTopBar(
    onLanguageChange: (lang: String) -> Unit,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "Search Movies , Tv Shows and more.",
                style = TextStyle(fontSize = 16.sp, color = Color.White)
            )
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = Color.White)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    onLanguageChange("en")
                    expanded = false
                }) {
                    Text("English")
                }
                DropdownMenuItem(onClick = {
                    onLanguageChange("ar")
                    expanded = false
                }) {
                    Text("Arabic")
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF2342)),
    )
}
