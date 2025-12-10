package com.example.jessapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jessapp.Movie
import com.example.jessapp.Person
import com.example.jessapp.Series

@Entity(tableName = "fav_movies")
data class FilmEntity(
    @PrimaryKey val id: Int,
    val fiche: Movie
)

@Entity(tableName = "fav_series")
data class SerieEntity(
    @PrimaryKey val id: Int,
    val fiche: Series
)

@Entity(tableName = "fav_actors")
data class ActeurEntity(
    @PrimaryKey val id: Int,
    val fiche: Person
)