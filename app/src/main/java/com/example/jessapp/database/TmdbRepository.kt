package com.example.jessapp

import com.example.jessapp.database.ActeurEntity
import com.example.jessapp.database.FilmEntity
import com.example.jessapp.database.SerieEntity
import com.example.jessapp.database.TmdbDao
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val tmdbAPI: TmdbAPI,
    private val tmdbDao: TmdbDao
) {
    // Votre clé API (idéalement à mettre dans le local.properties, mais ok ici pour l'exercice)
    private val apiKey = "921cce22156ad67dc3690d7c881e4037"

    // ===================================================================================
    // FILMS
    // ===================================================================================

    // Récupère les films populaires et coche "isFav" si le film est en base
    suspend fun getMovies(): List<Movie> {
        val apiMovies = tmdbAPI.getLastMovies(apiKey).results
        val favMoviesIds = tmdbDao.getFavMovies().map { it.id } // On récupère juste les IDs des favoris

        return apiMovies.map { movie ->
            if (favMoviesIds.contains(movie.id)) {
                movie.copy(isFav = true)
            } else {
                movie
            }
        }
    }

    // Recherche de films + vérification favoris
    suspend fun searchMovies(query: String): List<Movie> {
        val apiMovies = tmdbAPI.searchMovies(apiKey, query).results
        val favMoviesIds = tmdbDao.getFavMovies().map { it.id }

        return apiMovies.map { movie ->
            if (favMoviesIds.contains(movie.id)) movie.copy(isFav = true) else movie
        }
    }

    // Ajoute un film aux favoris
    suspend fun addFavoriteMovie(movie: Movie) {
        tmdbDao.addMovie(FilmEntity(movie.id, movie))
    }

    // Supprime un film des favoris
    suspend fun removeFavoriteMovie(id: Int) {
        tmdbDao.deleteMovie(id)
    }

    // Récupère UNIQUEMENT les films favoris (depuis la base locale)
    suspend fun getFavoriteMovies(): List<Movie> {
        return tmdbDao.getFavMovies().map { it.fiche.copy(isFav = true) }
    }

    // Récupère le détail d'un film (API)
    suspend fun getMovieDetail(id: Int): Movie {
        return tmdbAPI.getMovieDetail(id, apiKey)
    }

    suspend fun getMovieCast(id: Int): List<Cast> {
        return tmdbAPI.getMovieCredits(id, apiKey).cast
    }


    // ===================================================================================
    // SÉRIES
    // ===================================================================================

    suspend fun getSeries(): List<Series> {
        val apiSeries = tmdbAPI.getLastSeries(apiKey).results
        val favSeriesIds = tmdbDao.getFavSeries().map { it.id }

        return apiSeries.map { serie ->
            if (favSeriesIds.contains(serie.id)) serie.copy(isFav = true) else serie
        }
    }

    suspend fun searchSeries(query: String): List<Series> {
        val apiSeries = tmdbAPI.searchSeries(apiKey, query).results
        val favSeriesIds = tmdbDao.getFavSeries().map { it.id }

        return apiSeries.map { serie ->
            if (favSeriesIds.contains(serie.id)) serie.copy(isFav = true) else serie
        }
    }

    suspend fun addFavoriteSeries(series: Series) {
        tmdbDao.addSeries(SerieEntity(series.id, series))
    }

    suspend fun removeFavoriteSeries(id: Int) {
        tmdbDao.deleteSeries(id)
    }

    suspend fun getFavoriteSeries(): List<Series> {
        return tmdbDao.getFavSeries().map { it.fiche.copy(isFav = true) }
    }

    suspend fun getSeriesDetail(id: Int): Series {
        return tmdbAPI.getSeriesDetail(id, apiKey)
    }

    suspend fun getSeriesCast(id: Int): List<Cast> {
        return tmdbAPI.getSeriesCredits(id, apiKey).cast
    }


    // ===================================================================================
    // ACTEURS
    // ===================================================================================

    suspend fun getActors(): List<Person> {
        val apiActors = tmdbAPI.getLastPersons(apiKey).results
        val favActorsIds = tmdbDao.getFavActors().map { it.id }

        return apiActors.map { actor ->
            if (favActorsIds.contains(actor.id)) actor.copy(isFav = true) else actor
        }
    }

    suspend fun searchActors(query: String): List<Person> {
        val apiActors = tmdbAPI.searchPersons(apiKey, query).results
        val favActorsIds = tmdbDao.getFavActors().map { it.id }

        return apiActors.map { actor ->
            if (favActorsIds.contains(actor.id)) actor.copy(isFav = true) else actor
        }
    }

    suspend fun addFavoriteActor(person: Person) {
        tmdbDao.addActor(ActeurEntity(person.id, person))
    }

    suspend fun removeFavoriteActor(id: Int) {
        tmdbDao.deleteActor(id)
    }

    suspend fun getFavoriteActors(): List<Person> {
        return tmdbDao.getFavActors().map { it.fiche.copy(isFav = true) }
    }

    // Dans TmdbRepository
    suspend fun getPersonDetail(id: Int): PersonDetail {
        return tmdbAPI.getPersonDetail(id, apiKey)
    }
}