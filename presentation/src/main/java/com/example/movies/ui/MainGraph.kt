package com.example.movies.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.domain.model.Media
import com.example.movies.common.CustomNavType
import com.example.movies.ui.search.MediaDetailsPage
import com.example.movies.ui.search.SearchMediaPage
import com.example.movies.vm.SearchMoviesViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

@Serializable
data object SearchMediaRoute

@Serializable
data class MediaDetailRoute(val media: Media)

@Composable
fun MainGraph(
    padding: PaddingValues,
    navController: NavHostController
) {
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
            // sending custom object to detail screen
            val arguments = it.toRoute<MediaDetailRoute>()
            MediaDetailsPage(arguments.media)
        }
    }
}