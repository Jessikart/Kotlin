package com.example.jessapp

import MainViewModel

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
//import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import coil3.compose.AsyncImage
import com.example.jessapp.ui.theme.JessAppTheme

sealed class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    object Profile : AppScreen("profile", "Profil", Icons.Default.Person)
    object Films : AppScreen("films", "Films", Icons.Default.Movie)
    object Series : AppScreen("series", "Séries", Icons.Default.Tv)
    object Acteurs : AppScreen("acteurs", "Acteurs", Icons.Default.Face)
}

class ProfileDest
class FilmsDest
class SeriesDest
class ActeursDest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Any>(ProfileDest()) }
            val viewModel: MainViewModel = viewModel()

            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            val windowSizeClass = calculateWindowSizeClass(this)

            val bottomNavItems = listOf(
                AppScreen.Films,
                AppScreen.Series,
                AppScreen.Acteurs
            )



            val showBars = backStack.lastOrNull() !is ProfileDest

            JessAppTheme {
                Scaffold(
                    topBar = {
                        if (showBars) {
                            TmdbTopAppBar(
                                backStack = backStack,
                                canNavigateBack = false,
                                navigateUp = { },
                                onSearch = { query ->
                                    // Appel au ViewModel selon l'écran actif
                                    when (backStack.lastOrNull()) {
                                        is FilmsDest -> viewModel.searchMovies(query)/* on est sur la première destination */
                                        is SeriesDest -> viewModel.searchSeries(query)/* on est sur la deuxième */
                                        is ActeursDest ->  viewModel.searchActors(query)/* on est sur la troisième */
                                    }

                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBars) {
                            NavigationBar(containerColor = Color(0xFF6200EE)) {

                                    NavigationBarItem(
                                        icon = { Icon(painter = painterResource(R.drawable.outline_airline_seat_recline_normal_24), contentDescription = "Profil") },
                                        label = { Text("Profile") },
                                        selected = backStack.lastOrNull() is ProfileDest,
                                        onClick = {
                                            backStack.add(ProfileDest())
                                        }
                                    )
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_airline_seat_recline_normal_24), contentDescription = "Profil") },
                                    label = { Text("Films") },
                                    selected = backStack.lastOrNull() is FilmsDest,
                                    onClick = {
                                        backStack.add(FilmsDest())
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_airline_seat_recline_normal_24), contentDescription = "Profil") },
                                    label = { Text("Series") },
                                    selected = backStack.lastOrNull() is SeriesDest,
                                    onClick = {
                                        backStack.add(SeriesDest())
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(painter = painterResource(R.drawable.outline_airline_seat_recline_normal_24), contentDescription = "Profil") },
                                    label = { Text("Acteurs") },
                                    selected = backStack.lastOrNull() is ActeursDest,
                                    onClick = {
                                        backStack.add(ActeursDest())
                                    }
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
                            entry<FilmsDest> { Films(viewModel) }
                            entry<SeriesDest> { Series(viewModel) }
                            entry<ActeursDest> { Acteurs(viewModel) }
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
    backStack:  SnapshotStateList<Any>,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onSearch: (String) -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    if (isSearching) {
        TopAppBar(
            title = {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Rechercher un Films...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSearch(searchText)
                            isSearching = false
                        }
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearching = false }) {
                    Icon(Icons.Filled.ArrowBack, "Fermer la recherche")
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
            title = { Text("Films", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            },
            actions = {
                // Bouton de recherche disponible sur Films, Séries et Acteurs
                IconButton(onClick = { isSearching = true }) {
                    Icon(Icons.Filled.Search, contentDescription = "Rechercher")
                }
            }
        )
    }
}

// --- ÉCRAN FILMS ---
@Composable
fun Films(viewModel: MainViewModel) { // CORRECTION : Suppression de <Any?>
    val movies by viewModel.movies.collectAsState()

    LaunchedEffect(Unit) {
        if (movies.isEmpty()) {
            viewModel.getMovies()
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = movie.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )
                            Text(
                                text = movie.release_date.takeIf { !it.isNullOrBlank() } ?: "Date inconnue",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- ÉCRAN SÉRIES ---
@Composable
fun Series(viewModel: MainViewModel) { // CORRECTION : Suppression de <Any?>
    val series by viewModel.series.collectAsState()

    LaunchedEffect(Unit) {
        if (series.isEmpty()) {
            viewModel.getSeries()
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(series) { serie ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${serie.poster_path}",
                            contentDescription = serie.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = serie.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )
                            Text(
                                text = serie.first_air_date.takeIf { !it.isNullOrBlank() } ?: "Date inconnue",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- ÉCRAN ACTEURS ---
@Composable
fun Acteurs(viewModel: MainViewModel) { // CORRECTION : Suppression de <Any?>
    val actors by viewModel.actors.collectAsState()

    LaunchedEffect(Unit) {
        if (actors.isEmpty()) {
            viewModel.getActors()
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(actors) { actor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${actor.profile_path}",
                            contentDescription = actor.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = actor.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}


// --- ÉCRAN PROFIL ---
@Composable
fun Screen(classes: WindowSizeClass, onNavigate: () -> Unit ) {
    Log.d("monapp","screen")
    // CORRECTION 1 : Utilisez .widthSizeClass au lieu de .windowWidthSizeClass
    when (classes.widthSizeClass) {
        // CORRECTION 2 : Utilisez .Compact au lieu de .COMPACT
        WindowWidthSizeClass.Compact -> {
            VerticalProfileLayout(onNavigate)
        }
        else -> {
            HorizontalProfileLayout(onNavigate)
        }
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
            painter = painterResource(R.drawable.k2), // Vérifiez que cette image existe bien
            contentDescription = "Mon image de profil",
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Jessika MARTIN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Etudiante en BUT MMI",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "IUT PAUL SABATIER",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            ContactRow(
                icon = R.drawable.baseline_email_24, // Vérifiez que cette icône existe
                text = "jessika.martin@etu.iut-tlse3.fr",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(
                icon = R.drawable.linkedin, // Vérifiez que cette icône existe
                text = "www.linkedin.com/in/jessika-martin",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        Button(
            onClick = { onNavigate()  },
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
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = 16.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.k2),
                contentDescription = "Mon image de profil",
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Jessika MARTIN", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Etudiante en BUT MMI", fontSize = 14.sp)
            Text(text = "IUT PAUL SABATIER", fontSize = 14.sp)
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 16.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            ContactRow(
                icon = R.drawable.baseline_email_24,
                text = "jessika.martin@etu.iut-tlse3.fr"
            )
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(
                icon = R.drawable.linkedin,
                text = "www.linkedin.com/in/jessika-martin"
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigate() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Démarrer", color = Color.White)
            }
        }
    }
}

@Composable
fun ContactRow(icon: Int, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.DarkGray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}