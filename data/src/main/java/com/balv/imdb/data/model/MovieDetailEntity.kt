package com.balv.imdb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "movie_details")
@TypeConverters(
    ListStringConverter::class,
    ProductionCompanyListConverter::class,
    ProductionCountryListConverter::class,
    SpokenLanguageListConverter::class,
    GenreListConverter::class // Add the new TypeConverter
)
data class MovieDetailEntity(
    @PrimaryKey
    val id: Int,

    val adult: Boolean,
    val backdropPath: String?,

    val collectionId: Int?,
    val collectionName: String?,
    val collectionPosterPath: String?,
    val collectionBackdropPath: String?,

    val budget: Long,

    val genres: List<GenreLocal>, // <<<< Store genres directly here

    val homepage: String?,
    val imdbId: String?,
    val originCountryList: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompanyLocal>,
    val productionCountries: List<ProductionCountryLocal>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguageLocal>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val lastRefreshed: Long = System.currentTimeMillis()
)

object GenreListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(value: List<GenreLocal>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toGenreList(value: String?): List<GenreLocal>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<GenreLocal>>() {}.type) }
    }
}

object ListStringConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) }
    }
}

object ProductionCompanyListConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromProductionCompanyList(value: List<ProductionCompanyLocal>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toProductionCompanyList(value: String?): List<ProductionCompanyLocal>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<ProductionCompanyLocal>>() {}.type) }
    }
}

object ProductionCountryListConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromProductionCountryList(value: List<ProductionCountryLocal>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toProductionCountryList(value: String?): List<ProductionCountryLocal>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<ProductionCountryLocal>>() {}.type) }
    }
}

object SpokenLanguageListConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromSpokenLanguageList(value: List<SpokenLanguageLocal>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSpokenLanguageList(value: String?): List<SpokenLanguageLocal>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<SpokenLanguageLocal>>() {}.type) }
    }
}

data class ProductionCompanyLocal(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String?
)

data class ProductionCountryLocal(
    val iso31661: String,
    val name: String
)

data class SpokenLanguageLocal(
    val englishName: String,
    val iso6391: String,
    val name: String
)

data class GenreLocal(
    val id: Int,
    val name: String
)
