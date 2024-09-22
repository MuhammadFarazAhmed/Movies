package com.example.movies.ui.search

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.domain.model.Media
import com.example.movies.BuildConfig
import com.example.movies.R
import com.example.movies.theme.MoviesTheme
import com.example.movies.vm.MediaDetailViewModel
import com.example.movies.vm.MovieDetailsState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaDetailsPage(
    media: Media,
    mainNavController: NavHostController,
    vm: MediaDetailViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsState()
    state.media = media
    Log.d("TAG", "MediaDetailsPage: $media")
    MovieDetailsScreen(state, mainNavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailsState,
    appNavController: NavHostController
) {
    var animationVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animationVisible = true
    }


    Column(
        Modifier
            .fillMaxSize(1f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            SubcomposeAsyncImage(
                model = BuildConfig.IMAGE_BASE_URL + state.media.backdropPath,
                loading = { MovieItemPlaceholder() },
                error = { MovieItemPlaceholder() },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth(1f)
            )
            takeIf { state.media.video == true }?.let {
                PlayButton {

                }
            }
        }

        AnimatedVisibility(
            visible = animationVisible,
            enter = fadeIn(),
        ) {
            Text(
                text = state.media.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp, 16.dp, 16.dp, 0.dp)
                    .fillMaxWidth(1f),
            )
        }

        AnimatedVisibility(
            visible = animationVisible,
        ) {
            Text(
                text = state.media.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth(1f),
            )
        }

    }
}


@Composable
private fun MovieItemPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.placeholder),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun PlayButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Filled.PlayArrow,
        contentDescription = "Play Video",
        modifier = Modifier
            .size(64.dp) // Adjust size as needed
            .clickable(onClick = onClick)
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MovieDetailsScreenPreview() {
    PreviewContainer {
        MovieDetailsScreen(
            MovieDetailsState(
                Media(
                    id = 1,
                    name = "Avatar",
                    backdropPath = "https://i.stack.imgur.com/lDFzt.jpg",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.",
                    mediaType = "Movie",
                    video = false,
                    adult = false
                )
            ),
            rememberNavController()
        )
    }
}

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    MoviesTheme {
        content()
    }
}
