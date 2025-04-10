package com.example.movies.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.DrawableCompat.setLayoutDirection
import androidx.navigation.NavHostController
import com.example.movies.R
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
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                selectionColors = TextSelectionColors(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.primaryContainer
                ),
            )

        )
    }
}

@Composable
fun LocalizeApp(localeState: MutableState<Locale>, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val configuration = Configuration(context.resources.configuration).apply {
        setLocale(localeState.value)
        setLayoutDirection(localeState.value)
    }

    val localizedContext = context.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides if (localeState.value.language == "ar")
            LayoutDirection.Rtl else LayoutDirection.Ltr
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
@OptIn(ExperimentalMaterial3Api::class)
fun AppTopBar(
    showBackButton: Boolean = false,
    onLanguageChange: (lang: String) -> Unit = {},
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.search_movies),
                style = TextStyle(fontSize = 16.sp, color = Color.White)
            )
        },
        actions = {
            LanguageDropdown(expanded, onExpandedChange = { expanded = it }, onLanguageChange)
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF2342)),
    )
}

@Composable
fun LanguageDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    IconButton(onClick = { onExpandedChange(true) }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = Color.White)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) }
    ) {
        DropdownMenuItem(
            text = { Text("English") },
            onClick = {
                onLanguageChange("en")
                onExpandedChange(false)
            }
        )
        DropdownMenuItem(
            text = { Text("Arabic") },
            onClick = {
                onLanguageChange("ar")
                onExpandedChange(false)
            }
        )
    }
}