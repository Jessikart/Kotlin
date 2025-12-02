package com.example.jessapp

// --- FILMS (Déjà existant) ---
data class MovieList(
    val page: Int = 0,
    val results: List<Movie> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

data class Movie(
    val adult: Boolean = false,
    val backdrop_path: String = "",
    val genre_ids: List<Int> = listOf(),
    val id: Int = 0,
    val media_type: String = "",
    val original_language: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
)

// --- SÉRIES (Nouveau) ---
data class SeriesList(
    val page: Int = 0,
    val results: List<Series> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

data class Series(
    val id: Int = 0,
    val name: String = "",           // Attention: "name" au lieu de "title"
    val original_name: String = "",
    val overview: String = "",
    val poster_path: String = "",
    val backdrop_path: String = "",
    val first_air_date: String = "", // Attention: "first_air_date" au lieu de "release_date"
    val popularity: Double = 0.0,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val origin_country: List<String> = listOf()
)

// --- ACTEURS / PERSONNES (Nouveau) ---
data class PersonList(
    val page: Int = 0,
    val results: List<Person> = listOf(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)

data class Person(
    val id: Int = 0,
    val name: String = "",
    val original_name: String = "",
    val profile_path: String = "",   // Attention: "profile_path" au lieu de "poster_path"
    val gender: Int = 0,
    val popularity: Double = 0.0,
    val known_for_department: String = ""
    // On peut ajouter "known_for" si on veut la liste des films de l'acteur, mais c'est une liste complexe
)

data class CreditsResult(
    val cast: List<Cast> = emptyList()
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profile_path: String?
)