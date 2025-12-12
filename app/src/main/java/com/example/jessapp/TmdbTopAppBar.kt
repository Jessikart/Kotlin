package com.example.jessapp

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmdbTopAppBar(
    backStack: SnapshotStateList<Any>,
    canNavigateBack: Boolean,
    showOnlyFavorites: Boolean,
    navigateUp: () -> Unit,
    onSearch: (String) -> Unit,
    onToggleGlobalFilter: () -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(searchText) {
        if (searchText.length >= 3) {
            delay(500)
            onSearch(searchText)
        }
    }


    val title = when (backStack.lastOrNull()) {
        is FilmsDest -> "Films"
        is SeriesDest -> "Séries"
        is ActeursDest -> "Acteurs"
        else -> "Fav'App"
    }

    if (isSearching) {
        TopAppBar(
            title = {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Rechercher...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onSearch(searchText)
                        // On garde le clavier ouvert ou non selon votre préférence
                    }),
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(Icons.Filled.Close, "Effacer")
                            }
                        }
                    }
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    isSearching = false
                    searchText = ""
                    onSearch("")
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Fermer")
                }
            },
            actions = {
                IconButton(onClick = { onSearch(searchText) }) {
                    Icon(Icons.Filled.Search, "Valider")
                }
            }
        )
    } else {
        TopAppBar(
            title = { Text(title, style = MaterialTheme.typography.titleLarge) },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                    }
                }
            },
            actions = {

                IconButton(onClick = onToggleGlobalFilter) {
                    Icon(
                        imageVector = if (showOnlyFavorites) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Filtre Favoris",
                        tint = if (showOnlyFavorites) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { isSearching = true }) {
                    Icon(Icons.Filled.Search, "Rechercher")
                }
            }
        )
    }
}