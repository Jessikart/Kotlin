package com.example.jessapp.di

import android.content.Context
import androidx.room.Room
import com.example.jessapp.TmdbAPI // Assurez-vous que cet import est correct
import com.example.jessapp.database.AppDatabase
import com.example.jessapp.database.Converters
import com.example.jessapp.database.TmdbDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. Fournit Moshi (JSON)
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // 2. Fournit les Convertisseurs pour Room
    @Singleton
    @Provides
    fun provideConverters(moshi: Moshi): Converters {
        return Converters(moshi)
    }

    // 3. Fournit la Base de données Room
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: Converters
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tmdb_database"
        )
            .addTypeConverter(converters)
            .fallbackToDestructiveMigration()
            .build()
    }

    // 4. Fournit le DAO (Accès données locales)
    @Singleton
    @Provides
    fun provideTmdbDao(database: AppDatabase): TmdbDao {
        return database.dao()
    }

    // --- C'EST ICI QUE MANQUAIT LE CODE POUR CORRIGER L'ERREUR ---

    // 5. Fournit l'API Retrofit (Accès données Internet)
    @Singleton
    @Provides
    fun provideTmdbApi(moshi: Moshi): TmdbAPI {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            // On utilise l'instance Moshi qu'on a déjà créée plus haut
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TmdbAPI::class.java)
    }
}