package com.example.jessapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TmdbRepository
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

    // --- ÉTAT FILTRE
    val showOnlyFavorites = MutableStateFlow(false)

    init {
        refreshAll()
    }

    private fun refreshAll() {
        getMovies()
        getSeries()
        getActors()
    }


    fun getMovies() {
        viewModelScope.launch {
            try {
                if (showOnlyFavorites.value) {

                    movies.value = repository.getFavoriteMovies()
                } else {

                    movies.value = repository.getMovies()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {

                movies.value = repository.searchMovies(query)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // Gestion du clic sur le cœur
    fun toggleFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            if (movie.isFav) {
                repository.removeFavoriteMovie(movie.id)
            } else {
                repository.addFavoriteMovie(movie.copy(isFav = true))
            }

            getMovies()

            if (selectedMovie.value?.id == movie.id) {
                selectedMovie.value = movie.copy(isFav = !movie.isFav)
            }
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            try {

                val movie = repository.getMovieDetail(id)
                val favs = repository.getFavoriteMovies()
                val isFav = favs.any { it.id == id }

                selectedMovie.value = movie.copy(isFav = isFav)
                currentCast.value = repository.getMovieCast(id)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }



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



    fun toggleGlobalFilter() {
        showOnlyFavorites.value = !showOnlyFavorites.value
        refreshAll()
    }

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