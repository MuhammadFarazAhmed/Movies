package com.example.movies.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Media
import com.example.movies.BuildConfig
import com.example.movies.R
import com.example.movies.vm.SearchMoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(vm: SearchMoviesViewModel = koinViewModel()) {
    val media = vm.media.collectAsState()

    Surface(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        Column {
            SearchView(
                onQueryChange = {
                    vm.onQueryChange(it)
                },
                onBackClick = {
                    vm.onQueryChange("")
                }
            )

            LazyColumn {
                media.value.forEach { (mediaType, mediaList) ->

                    item {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = mediaType.capitalize(Locale.current),
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(mediaList.size) { index ->
                                MediaItem(mediaList, index)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaItem(
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

@Preview
@Composable
fun SearchScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            SearchView(onQueryChange = {}, onBackClick = {})

            LazyColumn {
                item {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "Movies",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                }

                val list = listOf(
                    Media(1, false, "/ksi3j.jpd", "movie"),
                    Media(1, false, "/ksi3j.jpd", "movie"),
                    Media(1, false, "/ksi3j.jpd", "movie")
                )
                item {
                    LazyRow(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(list.size) { index ->
                            MediaItem(list, index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    onQueryChange: (query: String) -> Unit,
    onBackClick: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(text = "Search")
            },
            value = query,
            onValueChange = {
                query = it
                onQueryChange(it)
            }, leadingIcon = {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        query = ""
                        onQueryChange("")
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                cursorColor = colors.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                selectionColors = TextSelectionColors(colors.onPrimary, colors.primaryVariant),
            )

        )
    }
}