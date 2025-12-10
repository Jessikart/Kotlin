package com.example.jessapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FilmEntity::class, SerieEntity::class, ActeurEntity::class],
    version = 1,
    exportSchema = false // On met false pour éviter un warning de build
)
@TypeConverters(Converters::class) // On attache nos convertisseurs Moshi ici
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): TmdbDao
}