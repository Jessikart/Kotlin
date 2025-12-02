package com.example.jessapp

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPI {

    // --- FILMS ---
    @GET("trending/movie/week")
    suspend fun getLastMovies(@Query("api_key") api_key: String, @Query("language") language: String = "fr"): MovieList

    @GET("search/movie")
    suspend fun searchMovies(@Query("api_key") api_key: String, @Query("query") query: String, @Query("language") language: String = "fr"): MovieList

    // --- SÉRIES ---
    @GET("trending/tv/week")
    suspend fun getLastSeries(@Query("api_key") api_key: String, @Query("language") language: String = "fr"): SeriesList

    @GET("search/tv")
    suspend fun searchSeries(@Query("api_key") api_key: String, @Query("query") query: String, @Query("language") language: String = "fr"): SeriesList

    // --- ACTEURS ---
    @GET("trending/person/week")
    suspend fun getLastPersons(@Query("api_key") api_key: String, @Query("language") language: String = "fr"): PersonList

    @GET("search/person")
    suspend fun searchPersons(@Query("api_key") api_key: String, @Query("query") query: String, @Query("language") language: String = "fr"): PersonList
}