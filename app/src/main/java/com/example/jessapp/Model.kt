package com.example.jessapp

import kotlinx.serialization.Serializable

// --- FILMS ---
@Serializable
data class MovieList(
    val page: Int = 0,
    val results: List<Movie> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    // CORRECTION : String? = null pour éviter le crash si pas d'image
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val release_date: String? = null,
    val overview: String = "",
    val popularity: Double = 0.0,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val adult: Boolean = false,
    val media_type: String = "",
    val original_language: String = "",
    val original_title: String = "",
    val video: Boolean = false,
    // Champs ajoutés manuellement (pas dans le JSON)
    var isFav: Boolean = false
)

// --- SÉRIES ---
@Serializable
data class SeriesList(
    val page: Int = 0,
    val results: List<Series> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

@Serializable
data class Series(
    val id: Int,
    val name: String,
    // CORRECTION : String? = null
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val overview: String = "",
    val original_name: String = "",
    val popularity: Double = 0.0,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val origin_country: List<String> = listOf(),
    var isFav: Boolean = false
)

// --- ACTEURS / PERSONNES ---
@Serializable
data class PersonList(
    val page: Int = 0,
    val results: List<Person> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

@Serializable
data class Person(
    val id: Int,
    val name: String,
    val original_name: String = "",
    // CORRECTION IMPORTANTE : profile_path PEUT ÊTRE NULL
    val profile_path: String? = null,
    val gender: Int = 0,
    val popularity: Double = 0.0,
    val known_for_department: String = "",
    var isFav: Boolean = false
)

// --- CRÉDITS (DÉTAILS) ---
@Serializable
data class CreditsResult(
    val cast: List<Cast> = emptyList()
)

@Serializable
data class Cast(
    val id: Int,
    val name: String,
    val character: String = "",
    val profile_path: String? = null
)