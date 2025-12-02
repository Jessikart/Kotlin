package com.example.jessapp

import MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

    // On charge les infos dès l'ouverture de l'écran
    LaunchedEffect(movieId) {
        viewModel.getMovieDetail(movieId)
    }

    // On affiche seulement si le film est chargé
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

    LaunchedEffect(seriesId) {
        viewModel.getSeriesDetail(seriesId)
    }

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

// --- DESIGN COMMUN (Le layout) ---
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
            // TopBar transparente avec bouton retour
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        // LazyColumn permet de scroller tout l'écran
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)
        ) {
            // 1. HEADER (Image de fond + Titre)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Hauteur de l'image de fond
                ) {
                    // Image de fond (Backdrop)
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780$backdropPath",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Affiche (Poster) superposée
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.White),
                                    startY = 500f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black // Ou White selon ton thème
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sortie : ${releaseDate ?: "Inconnue"}",
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            // 2. SYNOPSIS
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Synopsis",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (overview.isNotBlank()) overview else "Aucun résumé disponible.",
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            // 3. CASTING (Défilement horizontal)
            item {
                if (cast.isNotEmpty()) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = "Têtes d'affiche",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cast) { actor ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(100.dp)
                                ) {
                                    Card(
                                        shape = CircleShape, // Photo ronde comme sur les contacts
                                        modifier = Modifier.size(100.dp)
                                    ) {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w185${actor.profile_path}",
                                            contentDescription = actor.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(android.R.drawable.ic_menu_camera)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = actor.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                    Text(
                                        text = actor.character,
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Espace en bas pour ne pas être caché par la barre de navigation
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}