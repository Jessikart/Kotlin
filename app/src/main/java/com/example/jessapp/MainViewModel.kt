package com.example.jessapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Indispensable pour que Hilt sache créer ce ViewModel
class MainViewModel @Inject constructor(
    private val repository: TmdbRepository // On injecte le Repository, plus d'API directe
) : ViewModel() {

    // --- ÉTATS DES LISTES ---
    val movies = MutableStateFlow<List<Movie>>(emptyList())
    val series = MutableStateFlow<List<Series>>(emptyList())
    val actors = MutableStateFlow<List<Person>>(emptyList())

    // --- ÉTATS DÉTAILS ---
    val selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedSeries = MutableStateFlow<Series?>(null)
    val currentCast = MutableStateFlow<List<Cast>>(emptyList())

    // Variable d'état
    val selectedPerson = MutableStateFlow<PersonDetail?>(null)

    // --- ÉTAT FILTRE (Tout afficher vs Favoris uniquement) ---
    val showOnlyFavorites = MutableStateFlow(false)

    // Initialisation : on charge les données au lancement
    init {
        refreshAll()
    }

    // Fonction centrale pour recharger les données selon le filtre actif
    private fun refreshAll() {
        getMovies()
        getSeries()
        getActors()
    }

    // ===================================================================================
    // LOGIQUE FILMS
    // ===================================================================================

    fun getMovies() {
        viewModelScope.launch {
            try {
                if (showOnlyFavorites.value) {
                    // Si filtre activé : on prend juste la DB locale
                    movies.value = repository.getFavoriteMovies()
                } else {
                    // Sinon : on prend l'API (qui vérifie aussi les favoris)
                    movies.value = repository.getMovies()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                // La recherche se fait toujours sur l'API (ou filtre local si on veut pousser la logique)
                // Ici on reste simple : recherche API standard
                movies.value = repository.searchMovies(query)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // Gestion du clic sur le cœur (Toggle)
    fun toggleFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            if (movie.isFav) {
                repository.removeFavoriteMovie(movie.id)
            } else {
                // On ajoute une copie avec isFav = true
                repository.addFavoriteMovie(movie.copy(isFav = true))
            }
            // On rafraichit la liste pour voir le cœur changer de couleur
            getMovies()

            // Si on est sur l'écran de détail, on met à jour l'objet sélectionné aussi
            if (selectedMovie.value?.id == movie.id) {
                selectedMovie.value = movie.copy(isFav = !movie.isFav)
            }
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            try {
                // On récupère le détail
                val movie = repository.getMovieDetail(id)
                // On vérifie s'il est en favori dans la base pour cocher la case si besoin
                val favs = repository.getFavoriteMovies()
                val isFav = favs.any { it.id == id }

                selectedMovie.value = movie.copy(isFav = isFav)
                currentCast.value = repository.getMovieCast(id)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // ===================================================================================
    // LOGIQUE SÉRIES
    // ===================================================================================

    fun getSeries() {
        viewModelScope.launch {
            try {
                if (showOnlyFavorites.value) {
                    series.value = repository.getFavoriteSeries()
                } else {
                    series.value = repository.getSeries()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun searchSeries(query: String) {
        viewModelScope.launch {
            try {
                series.value = repository.searchSeries(query)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun toggleFavoriteSeries(serie: Series) {
        viewModelScope.launch {
            if (serie.isFav) {
                repository.removeFavoriteSeries(serie.id)
            } else {
                repository.addFavoriteSeries(serie.copy(isFav = true))
            }
            getSeries()

            if (selectedSeries.value?.id == serie.id) {
                selectedSeries.value = serie.copy(isFav = !serie.isFav)
            }
        }
    }

    fun getSeriesDetail(id: Int) {
        viewModelScope.launch {
            try {
                val serie = repository.getSeriesDetail(id)
                val favs = repository.getFavoriteSeries()
                val isFav = favs.any { it.id == id }

                selectedSeries.value = serie.copy(isFav = isFav)
                currentCast.value = repository.getSeriesCast(id)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // ===================================================================================
    // LOGIQUE ACTEURS
    // ===================================================================================

    fun getActors() {
        viewModelScope.launch {
            try {
                if (showOnlyFavorites.value) {
                    actors.value = repository.getFavoriteActors()
                } else {
                    actors.value = repository.getActors()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun searchActors(query: String) {
        viewModelScope.launch {
            try {
                actors.value = repository.searchActors(query)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun toggleFavoriteActor(person: Person) {
        viewModelScope.launch {
            if (person.isFav) {
                repository.removeFavoriteActor(person.id)
            } else {
                repository.addFavoriteActor(person.copy(isFav = true))
            }
            getActors()
        }
    }

    // ===================================================================================
    // FILTRE GLOBAL
    // ===================================================================================

    fun toggleGlobalFilter() {
        showOnlyFavorites.value = !showOnlyFavorites.value
        refreshAll() // Recharge toutes les listes avec le nouveau mode
    }

    // Fonction de chargement
    fun getPersonDetail(id: Int) {
        viewModelScope.launch {
            try {
                selectedPerson.value = repository.getPersonDetail(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}