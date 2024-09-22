package com.example.movies.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

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
                        imageVector = Icons.AutoMirrored.Filled.ManageSearch,
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


@Composable
fun LocalizeApp(language: String, content: @Composable () -> Unit) {
    val locale = Locale(language)
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)

    CompositionLocalProvider(
        LocalLayoutDirection provides
                if (LocalConfiguration.current.layoutDirection == LayoutDirection.Rtl.ordinal)
                    LayoutDirection.Rtl
                else LayoutDirection.Ltr
    ) {

        content()

    }
}

@Composable
fun LoaderFullScreen(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        CircularProgressIndicator()
    }
}