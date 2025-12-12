package com.example.jessapp.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.jessapp.Movie
import com.example.jessapp.Person
import com.example.jessapp.Series
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class Converters(private val moshi: Moshi) {

    // --- MOVIE ---
    private val movieAdapter = moshi.adapter(Movie::class.java)

    @TypeConverter
    fun fromMovie(movie: Movie): String {
        return movieAdapter.toJson(movie)
    }

    @TypeConverter
    fun toMovie(json: String): Movie? {
        return movieAdapter.fromJson(json)
    }

    // --- SERIES ---
    private val seriesAdapter = moshi.adapter(Series::class.java)

    @TypeConverter
    fun fromSeries(series: Series): String {
        return seriesAdapter.toJson(series)
    }

    @TypeConverter
    fun toSeries(json: String): Series? {
        return seriesAdapter.fromJson(json)
    }

    // --- PERSON ---
    private val personAdapter = moshi.adapter(Person::class.java)

    @TypeConverter
    fun fromPerson(person: Person): String {
        return personAdapter.toJson(person)
    }

    @TypeConverter
    fun toPerson(json: String): Person? {
        return personAdapter.fromJson(json)
    }
}