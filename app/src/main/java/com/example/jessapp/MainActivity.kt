package com.example.jessapp

import MainViewModel // Vérifie que ce fichier existe bien
// Si DetailScreen est dans un autre package, importe-le ici.
// Sinon, si c'est dans le même package (com.example.jessapp), c'est automatique.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

// --- DESTINATIONS ---
// Utilisation de classes simples avec equals/hashCode obligatoires pour Nav 3

class ProfileDest {
    override fun equals(other: Any?): Boolean = other is ProfileDest
    override fun hashCode(): Int = javaClass.hashCode()
}

class FilmsDest {
    override fun equals(other: Any?): Boolean = other is FilmsDest
    override fun hashCode(): Int = javaClass.hashCode()
}

class SeriesDest {
    override fun equals(other: Any?): Boolean = other is SeriesDest
    override fun hashCode(): Int = javaClass.hashCode()
}

class ActeursDest {
    override fun equals(other: Any?): Boolean = other is ActeursDest
    override fun hashCode(): Int = javaClass.hashCode()
}

// Destinations avec paramètres (ID)
class MovieDetailDest(val id: Int) {
    override fun equals(other: Any?): Boolean = other is MovieDetailDest && other.id == id
    override fun hashCode(): Int = id
}

class SeriesDetailDest(val id: Int) {
    override fun equals(other: Any?): Boolean = other is SeriesDetailDest && other.id == id
    override fun hashCode(): Int = id
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Any>(ProfileDest()) }
            val viewModel: MainViewModel = viewModel()
            val windowSizeClass = calculateWindowSizeClass(this)

            // Gestion du bouton retour physique
            BackHandler(enabled = backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }

            // On regarde le dernier écran pour savoir quoi afficher
            val currentDestination = backStack.lastOrNull()

            // On cache les barres sur le Profil ET sur les écrans de détail
            val showBars = currentDestination !is ProfileDest
                    && currentDestination !is MovieDetailDest
                    && currentDestination !is SeriesDetailDest

            JessAppTheme {
                Scaffold(
                    topBar = {
                        if (showBars) {
                            TmdbTopAppBar(
                                backStack = backStack,
                                canNavigateBack = backStack.size > 1,
                                navigateUp = { backStack.removeAt(backStack.lastIndex) },
                                onSearch = { query ->
                                    when (backStack.lastOrNull()) {
                                        is FilmsDest -> viewModel.searchMovies(query)
                                        is SeriesDest -> viewModel.searchSeries(query)
                                        is ActeursDest -> viewModel.searchActors(query)
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            NavigationBar(containerColor = Color(0xFF6200EE)) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Movie, contentDescription = "Films") },
                                    label = { Text("Films") },
                                    selected = backStack.lastOrNull() is FilmsDest,
                                    onClick = { if (backStack.lastOrNull() !is FilmsDest) backStack.add(FilmsDest()) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Tv, contentDescription = "Séries") },
                                    label = { Text("Séries") },
                                    selected = backStack.lastOrNull() is SeriesDest,
                                    onClick = { if (backStack.lastOrNull() !is SeriesDest) backStack.add(SeriesDest()) }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Face, contentDescription = "Acteurs") },
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

                            // 1. Profil
                            entry<ProfileDest> {
                                Screen(windowSizeClass) { backStack.add(FilmsDest()) }
                            }

                            // 2. Films (Avec clic vers détail)
                            entry<FilmsDest> {
                                Films(viewModel) { id ->
                                    backStack.add(MovieDetailDest(id))
                                }
                            }

                            // 3. Séries (Avec clic vers détail)
                            entry<SeriesDest> {
                                Series(viewModel) { id ->
                                    backStack.add(SeriesDetailDest(id))
                                }
                            }

                            // 4. Acteurs
                            entry<ActeursDest> {
                                Acteurs(viewModel)
                            }

                            // 5. Détail Film
                            entry<MovieDetailDest> { dest ->
                                MovieDetailScreen(
                                    movieId = dest.id,
                                    viewModel = viewModel,
                                    onBack = { backStack.removeAt(backStack.lastIndex) }
                                )
                            }

                            // 6. Détail Série
                            entry<SeriesDetailDest> { dest ->
                                SeriesDetailScreen(
                                    seriesId = dest.id,
                                    viewModel = viewModel,
                                    onBack = { backStack.removeAt(backStack.lastIndex) }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

// --- BARRE DE RECHERCHE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TmdbTopAppBar(
    backStack: SnapshotStateList<Any>,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onSearch: (String) -> Unit
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
                    keyboardActions = KeyboardActions(onDone = {
                        onSearch(searchText)
                        isSearching = false
                    })
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearching = false }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Fermer")
                }
            },
            actions = {
                IconButton(onClick = {
                    onSearch(searchText)
                    isSearching = false
                }) {
                    Icon(Icons.Filled.Search, "Valider")
                }
            }
        )
    } else {
        TopAppBar(
            title = { Text(title, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                    }
                }
            },
            actions = {
                IconButton(onClick = { isSearching = true }) {
                    Icon(Icons.Filled.Search, "Rechercher")
                }
            }
        )
    }
}

// --- LES ÉCRANS ---

@Composable
fun Films(viewModel: MainViewModel, onClick: (Int) -> Unit) { // Ajout du param onClick
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
                onClick = { onClick(movie.id) } // On passe l'ID
            )
        }
    }
}

@Composable
fun Series(viewModel: MainViewModel, onClick: (Int) -> Unit) { // Ajout du param onClick
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
                onClick = { onClick(serie.id) } // On passe l'ID
            )
        }
    }
}

@Composable
fun Acteurs(viewModel: MainViewModel) {
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
                subtitle = ""
                // Pas de onClick pour l'instant pour les acteurs
            )
        }
    }
}

@Composable
fun MediaCard(url: String, title: String, subtitle: String?, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClick() } // La carte devient cliquable
    ) {
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
    }
}

// --- ÉCRAN PROFIL ---
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(R.drawable.k2),
            contentDescription = "Mon image de profil",
            modifier = Modifier.size(250.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Jessika MARTIN", fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Etudiante en BUT MMI", fontSize = 16.sp, textAlign = TextAlign.Center)

            Text(text = "IUT PAUL SABATIER", fontSize = 14.sp, textAlign = TextAlign.Center)

        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            ContactRow(
                icon = R.drawable.baseline_email_24,
                text = "jessika.martin@etu.iut-tlse3.fr",
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(

                icon = R.drawable.linkedin,
                text = "www.linkedin.com/in/jessika-martin",
                modifier = Modifier.padding(horizontal = 20.dp)
            )

        }
        Button(
            onClick = { onNavigate() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
            modifier = Modifier.width(180.dp)
        ) {
            Text(text = "Démarrer", color = Color.White)
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
            contentDescription = "Mon image de profil",
            modifier = Modifier.size(250.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column {
            Text("Jessika MARTIN", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Etudiante en BUT MMI", fontSize = 14.sp)
            Text(text = "IUT PAUL SABATIER", fontSize = 14.sp)
            Button(onClick = { onNavigate() }) { Text(text = "Démarrer") }
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