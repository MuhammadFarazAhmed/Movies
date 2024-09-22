package com.example.movies.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Media
import com.example.movies.BuildConfig
import com.example.movies.R
import com.example.movies.common.EmptyStateIcon
import com.example.movies.common.EmptyStateView
import com.example.movies.common.LoaderFullScreen
import com.example.movies.common.SearchView
import com.example.movies.vm.SearchMediaState
import com.example.movies.vm.SearchMoviesViewModel


@Composable
fun SearchMediaPage(
    vm: SearchMoviesViewModel,
    onclick: (media: Media) -> Unit,
) {
    val state by vm.uiState.collectAsState()
    SearchMediaScreen(state, vm, onclick)
}


@Composable
fun SearchMediaScreen(
    state: SearchMediaState,
    vm: SearchMoviesViewModel,
    onclick: (media: Media) -> Unit
) {
    Surface(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        Column {
            Searchbar(vm)
            SearchList(state, onclick)
        }
    }
}

@Composable
private fun Searchbar(vm: SearchMoviesViewModel) {
    SearchView(
        onQueryChange = {
            vm.onQueryChange(it)
        },
        onBackClick = {
            vm.onQueryChange("")
        }
    )
}

@Composable
private fun SearchList(state: SearchMediaState, onclick: (Media) -> Unit) {

    when {
        state.loading -> {
            LoaderFullScreen()
        }

        state.media.isNotEmpty() -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                state.media.forEach { (mediaType, mediaList) ->
                    item {
                        CategoryHeading(mediaType)
                    }

                    item {
                        Carousel(mediaList = mediaList, onclick)
                    }
                }
            }
        }

        state.defaultState -> {
            EmptyStateView(
                icon = EmptyStateIcon(R.drawable.bg_empty_search),
                title = "Search Movies, tv and more",
                subtitle = "",
                verticalArrangement = Arrangement.Center
            )
        }

        state.emptyView -> {
            EmptyStateView(
                icon = EmptyStateIcon(R.drawable.bg_empty_no_result),
                title = "No Results Found",
                subtitle = "Search with different keyword",
                verticalArrangement = Arrangement.Center
            )
        }
    }
}


@Composable
private fun CategoryHeading(category: String) {
    Text(
        modifier = Modifier.padding(12.dp),
        text = category,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    )
}

@Composable
private fun Carousel(mediaList: List<Media>, onclick: (media: Media) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mediaList.size) { index ->
            CarousalItem(mediaList, index, onclick)
        }
    }
}

@Composable
private fun CarousalItem(
    mediaList: List<Media>,
    index: Int,
    onclick: (media: Media) -> Unit
) {
    AsyncImage(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .size(200.dp, 120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onclick(mediaList[index])
            },
        model = BuildConfig.IMAGE_BASE_URL + mediaList[index].backdropPath,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.placeholder),
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    PreviewContainer {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                SearchView(onQueryChange = {}, onBackClick = {})

                LazyColumn {
                    item {
                        CategoryHeading("Movies")
                    }

                    val list = listOf(
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false)
                    )
                    item {
                        Carousel(list) {

                        }
                    }

                    item {
                        CategoryHeading("Tv")
                    }

                    val tv = listOf(
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false)
                    )
                    item {
                        Carousel(tv) {

                        }
                    }
                }
            }
        }
    }
}

