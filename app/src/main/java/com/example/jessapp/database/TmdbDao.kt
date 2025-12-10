package com.example.jessapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TmdbDao {

    // --- FILMS ---
    @Query("SELECT * FROM fav_movies")
    suspend fun getFavMovies(): List<FilmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: FilmEntity)

    @Query("DELETE FROM fav_movies WHERE id = :id")
    suspend fun deleteMovie(id: Int)

    // --- SÉRIES ---
    @Query("SELECT * FROM fav_series")
    suspend fun getFavSeries(): List<SerieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSeries(serie: SerieEntity)

    @Query("DELETE FROM fav_series WHERE id = :id")
    suspend fun deleteSeries(id: Int)

    // --- ACTEURS ---
    @Query("SELECT * FROM fav_actors")
    suspend fun getFavActors(): List<ActeurEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addActor(actor: ActeurEntity)

    @Query("DELETE FROM fav_actors WHERE id = :id")
    suspend fun deleteActor(id: Int)
}