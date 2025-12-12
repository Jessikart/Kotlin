package com.example.jessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import com.example.jessapp.ui.theme.JessAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

// --- DESTINATIONS ---
@Serializable class ProfileDest {
    override fun equals(other: Any?): Boolean = other is ProfileDest
    override fun hashCode(): Int = javaClass.hashCode()
}
@Serializable class FilmsDest {
    override fun equals(other: Any?): Boolean = other is FilmsDest
    override fun hashCode(): Int = javaClass.hashCode()
}
@Serializable class SeriesDest {
    override fun equals(other: Any?): Boolean = other is SeriesDest
    override fun hashCode(): Int = javaClass.hashCode()
}
@Serializable class ActeursDest {
    override fun equals(other: Any?): Boolean = other is ActeursDest
    override fun hashCode(): Int = javaClass.hashCode()
}
@Serializable class MovieDetailDest(val id: Int) {
    override fun equals(other: Any?): Boolean = other is MovieDetailDest && other.id == id
    override fun hashCode(): Int = id
}
@Serializable class SeriesDetailDest(val id: Int) {
    override fun equals(other: Any?): Boolean = other is SeriesDetailDest && other.id == id
    override fun hashCode(): Int = id
}
@Serializable class ActorDetailDest(val id: Int) {
    override fun equals(other: Any?): Boolean = other is ActorDetailDest && other.id == id
    override fun hashCode(): Int = id
}

@AndroidEntryPoint // INDISPENSABLE POUR HILT
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Any>(ProfileDest()) }
            val viewModel: MainViewModel = viewModel()
            val windowSizeClass = calculateWindowSizeClass(this)

            // État du filtre "Favoris uniquement"
            val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()

            BackHandler(enabled = backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }

            val currentDestination = backStack.lastOrNull()
            // On cache les barres sur le profil ET les détails
            val showBars = currentDestination !is ProfileDest
                    && currentDestination !is MovieDetailDest
                    && currentDestination !is SeriesDetailDest
                    && currentDestination !is ActorDetailDest

            JessAppTheme {
                Scaffold(
                    topBar = {
                        if (showBars) {
                            TmdbTopAppBar(
                                backStack = backStack,
                                canNavigateBack = backStack.size > 1,
                                showOnlyFavorites = showOnlyFavorites, // On passe l'état du filtre
                                navigateUp = { backStack.removeAt(backStack.lastIndex) },
                                onSearch = { query ->
                                    when (backStack.lastOrNull()) {
                                        is FilmsDest -> viewModel.searchMovies(query)
                                        is SeriesDest -> viewModel.searchSeries(query)
                                        is ActeursDest -> viewModel.searchActors(query)
                                    }
                                },
                                onToggleGlobalFilter = { viewModel.toggleGlobalFilter() }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            NavigationBar(containerColor = Color(0xFF6200EE)) {
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_airplay_24), contentDescription = "Films") },
                                    label = { Text("Films") },
                                    selected = backStack.lastOrNull() is FilmsDest,
                                    onClick = { if (backStack.lastOrNull() !is FilmsDest) backStack.add(FilmsDest()) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_animated_images_24), contentDescription = "Series") },
                                    label = { Text("Series") },
                                    selected = backStack.lastOrNull() is SeriesDest,
                                    onClick = { if (backStack.lastOrNull() !is SeriesDest) backStack.add(SeriesDest()) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_boy_24), contentDescription = "Acteurs") },
                                    label = { Text("Acteurs") },
                                    selected = backStack.lastOrNull() is ActeursDest,
                                    onClick = { if (backStack.lastOrNull() !is ActeursDest) backStack.add(ActeursDest()) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        entryProvider = entryProvider {
                            entry<ProfileDest> { Screen(windowSizeClass) { backStack.add(FilmsDest()) } }

                            // FILMS
                            entry<FilmsDest> {
                                Films(
                                    viewModel = viewModel,
                                    onClick = { id -> backStack.add(MovieDetailDest(id)) }
                                )
                            }
                            // SÉRIES
                            entry<SeriesDest> {
                                Series(
                                    viewModel = viewModel,
                                    onClick = { id -> backStack.add(SeriesDetailDest(id)) }
                                )
                            }
                            // ACTEURS
                            entry<ActeursDest> {
                                Acteurs(
                                    viewModel = viewModel,
                                    onClick = { id -> backStack.add(ActorDetailDest(id)) }
                                )
                            }

                            // DÉTAILS
                            entry<MovieDetailDest> { dest ->
                                MovieDetailScreen(dest.id, viewModel) { backStack.removeAt(backStack.lastIndex) }
                            }
                            entry<SeriesDetailDest> { dest ->
                                SeriesDetailScreen(dest.id, viewModel) { backStack.removeAt(backStack.lastIndex) }
                            }
                            entry<ActorDetailDest> { dest ->
                                ActorDetailScreen(dest.id, viewModel) { backStack.removeAt(backStack.lastIndex) }
                            }
                        }
                    )
                }
            }
        }
    }
}

// --- TOP BAR (Avec filtre favoris) ---
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

    val title = when (backStack.lastOrNull()) {
        is FilmsDest -> "Films"
        is SeriesDest -> "Séries"
        is ActeursDest -> "Acteurs"
        else -> "App"
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
                    keyboardActions = KeyboardActions(onDone = { onSearch(searchText); isSearching = false })
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearching = false }) { Icon(Icons.Filled.ArrowBack, "Fermer") }
            },
            actions = {
                IconButton(onClick = { onSearch(searchText); isSearching = false }) { Icon(Icons.Filled.Search, "Valider") }
            }
        )
    } else {
        TopAppBar(
            title = { Text(title, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") }
                }
            },
            actions = {
                // BOUTON CŒUR FILTRE
                IconButton(onClick = onToggleGlobalFilter) {
                    Icon(
                        imageVector = if (showOnlyFavorites) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Filtre Favoris",
                        tint = if (showOnlyFavorites) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { isSearching = true }) { Icon(Icons.Filled.Search, "Rechercher") }
            }
        )
    }
}

// --- ÉCRANS (Avec gestion favoris et clic) ---

@Composable
fun Films(viewModel: MainViewModel, onClick: (Int) -> Unit) {
    val movies by viewModel.movies.collectAsState()
    LaunchedEffect(Unit) { if (movies.isEmpty()) viewModel.getMovies() }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MediaCard(
                url = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                title = movie.title,
                subtitle = movie.release_date,
                isFav = movie.isFav,
                onFavClick = { viewModel.toggleFavoriteMovie(movie) },
                onClick = { onClick(movie.id) }
            )
        }
    }
}

@Composable
fun Series(viewModel: MainViewModel, onClick: (Int) -> Unit) {
    val series by viewModel.series.collectAsState()
    LaunchedEffect(Unit) { if (series.isEmpty()) viewModel.getSeries() }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(series) { serie ->
            MediaCard(
                url = "https://image.tmdb.org/t/p/w500${serie.poster_path}",
                title = serie.name,
                subtitle = serie.first_air_date,
                isFav = serie.isFav,
                onFavClick = { viewModel.toggleFavoriteSeries(serie) },
                onClick = { onClick(serie.id) }
            )
        }
    }
}

@Composable
fun Acteurs(viewModel: MainViewModel, onClick: (Int) -> Unit) {
    val actors by viewModel.actors.collectAsState()
    LaunchedEffect(Unit) { if (actors.isEmpty()) viewModel.getActors() }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(actors) { actor ->
            MediaCard(
                url = "https://image.tmdb.org/t/p/w500${actor.profile_path}",
                title = actor.name,
                subtitle = "",
                isFav = actor.isFav,
                onFavClick = { viewModel.toggleFavoriteActor(actor) },
                onClick = { onClick(actor.id) }
            )
        }
    }
}

// --- MEDIA CARD (Avec Image et Cœur) ---
@Composable
fun MediaCard(
    url: String,
    title: String,
    subtitle: String?,
    isFav: Boolean,
    onFavClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = url,
                    contentDescription = title,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery)
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, maxLines = 2)
                    if (!subtitle.isNullOrBlank()) Text(subtitle, fontSize = 12.sp)
                }
            }

            // ICÔNE FAVORIS
            IconButton(
                onClick = onFavClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (isFav) Color.Red else Color.White
                )
            }
        }
    }
}

// --- ÉCRAN PROFIL (Inchangé) ---
@Composable
fun Screen(classes: WindowSizeClass, onNavigate: () -> Unit ) {
    when (classes.widthSizeClass) {
        WindowWidthSizeClass.Compact -> VerticalProfileLayout(onNavigate)
        else -> HorizontalProfileLayout(onNavigate)
    }
}

@Composable
fun VerticalProfileLayout(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(R.drawable.k2),
            contentDescription = "Profil",
            modifier = Modifier.size(250.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text("Jessika MARTIN", fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("Etudiante en BUT MMI", fontSize = 16.sp, textAlign = TextAlign.Center)
        Text("IUT PAUL SABATIER", fontSize = 14.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))
        Column {
            ContactRow(R.drawable.baseline_email_24, "jessika.martin@etu.iut-tlse3.fr")
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(R.drawable.linkedin, "www.linkedin.com/in/jessika-martin")
        }

        Button(onClick = onNavigate, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)), modifier = Modifier.width(180.dp)) {
            Text("Démarrer", color = Color.White)
        }
    }
}

@Composable
fun HorizontalProfileLayout(onNavigate: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.k2),
            contentDescription = "Profil",
            modifier = Modifier.size(250.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {
            Text("Jessika MARTIN", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { onNavigate() }) { Text("Démarrer") }
        }
    }
}

@Composable
fun ContactRow(icon: Int, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}