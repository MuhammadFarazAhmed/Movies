package com.example.movies.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.model.Media
import com.example.movies.BuildConfig
import com.example.movies.R
import com.example.movies.common.SearchView
import com.example.movies.vm.SearchMoviesViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchMediaPage(
    navController: NavHostController,
    vm: SearchMoviesViewModel = koinViewModel()
) {
    //val state by vm.uiState.collectAsState()
    SearchMediaScreen(navController, vm)
}


@Composable
fun SearchMediaScreen(
    navController: NavHostController,
    vm: SearchMoviesViewModel
) {
    Surface(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        Column {
            Searchbar(vm)
            SearchList(vm)
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
private fun SearchList(vm: SearchMoviesViewModel) {
    val media = vm.media.collectAsState()
    LazyColumn {
        media.value.forEach { (mediaType, mediaList) ->
            item {
                Category(mediaType)
            }

            item {
                Carousel(mediaList)
            }
        }
    }
}


@Composable
private fun Category(category: String) {
    Text(
        modifier = Modifier.padding(12.dp),
        text = category,
        style = TextStyle(
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    )
}

@Composable
private fun Carousel(list: List<Media>) {
    LazyRow(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list.size) { index ->
            CarousalItem(list, index)
        }
    }
}

@Composable
private fun CarousalItem(
    mediaList: List<Media>,
    index: Int
) {
    AsyncImage(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .size(200.dp, 120.dp)
            .clip(RoundedCornerShape(8.dp)),
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
                        Category("Movies")
                    }

                    val list = listOf(
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false),
                        Media(1, "", "", false, "/ksi3j.jpd", "movie", false)
                    )
                    item {
                        Carousel(list)
                    }
                }
            }
        }
    }
}

