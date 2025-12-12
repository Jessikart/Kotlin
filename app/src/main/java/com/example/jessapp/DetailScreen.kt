package com.example.jessapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

// --- ÉCRAN DÉTAIL FILM ---
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val movie by viewModel.selectedMovie.collectAsState()
    val cast by viewModel.currentCast.collectAsState()

    LaunchedEffect(movieId) { viewModel.getMovieDetail(movieId) }

    movie?.let { m ->
        DetailContent(
            backdropPath = m.backdrop_path,
            posterPath = m.poster_path,
            title = m.title,
            overview = m.overview,
            releaseDate = m.release_date,
            cast = cast,
            onBack = onBack
        )
    }
}

// --- ÉCRAN DÉTAIL SÉRIE ---
@Composable
fun SeriesDetailScreen(
    seriesId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val serie by viewModel.selectedSeries.collectAsState()
    val cast by viewModel.currentCast.collectAsState()

    LaunchedEffect(seriesId) { viewModel.getSeriesDetail(seriesId) }

    serie?.let { s ->
        DetailContent(
            backdropPath = s.backdrop_path,
            posterPath = s.poster_path,
            title = s.name,
            overview = s.overview,
            releaseDate = s.first_air_date,
            cast = cast,
            onBack = onBack
        )
    }
}

// --- ÉCRAN DÉTAIL ACTEUR (NOUVEAU) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailScreen(
    personId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val person by viewModel.selectedPerson.collectAsState()

    LaunchedEffect(personId) { viewModel.getPersonDetail(personId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(person?.name ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        person?.let { p ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Photo de profil
                Card(
                    modifier = Modifier
                        .height(300.dp)
                        .width(200.dp)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780${p.profile_path}",
                        contentDescription = p.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(android.R.drawable.ic_menu_camera)
                    )
                }

                // Nom
                Text(
                    text = p.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )

                // Infos naissance
                if (!p.birthday.isNullOrEmpty()) {
                    Text(
                        text = "Né(e) le : ${p.birthday}",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                }
                if (!p.place_of_birth.isNullOrEmpty()) {
                    Text(
                        text = "à ${p.place_of_birth}",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Biographie
                if (p.biography.isNotBlank()) {
                    Text(
                        text = "Biographie",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Text(
                        text = p.biography,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        lineHeight = 24.sp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// --- LAYOUT COMMUN POUR FILMS ET SÉRIES ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    backdropPath: String?,
    posterPath: String?,
    title: String,
    overview: String,
    releaseDate: String?,
    cast: List<Cast>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)) {
            // HEADER
            item {
                Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780$backdropPath",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.White), startY = 500f)
                    ))
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sortie : ${releaseDate ?: "Inconnue"}", fontSize = 14.sp, fontStyle = FontStyle.Italic)
                    }
                }
            }

            // SYNOPSIS
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Synopsis", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (overview.isNotBlank()) overview else "Aucun résumé.", fontSize = 16.sp, lineHeight = 24.sp)
                }
            }

            // CASTING
            item {
                if (cast.isNotEmpty()) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text("Têtes d'affiche", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(cast) { actor ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)) {
                                    Card(shape = CircleShape, modifier = Modifier.size(100.dp)) {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w185${actor.profile_path}",
                                            contentDescription = actor.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(android.R.drawable.ic_menu_camera)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(actor.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2)
                                    Text(actor.character, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, maxLines = 2)
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}