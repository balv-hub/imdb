package com.balv.imdb.data.model.entity

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
    GenreListConverter::class,
    CastMemberListConverter::class,
    CrewMemberListConverter::class,
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
    val popularity: Double?,
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
    val lastRefreshed: Long = System.currentTimeMillis(),
    val castMemberList: List<CastMemberLocal>? = emptyList(),
    val crewMemberList: List<CrewMemberLocal>? = emptyList(),
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

object CastMemberListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCastMemberList(value: List<CastMemberLocal>?): String? {
        // Only convert to JSON if the list is not null and not empty.
        // This stores NULL in the DB for empty lists, which toCastMemberList handles.
        return value?.takeIf { it.isNotEmpty() }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCastMemberList(value: String?): List<CastMemberLocal> { // Return non-nullable List
        return value?.let {
            val type = object : TypeToken<List<CastMemberLocal>>() {}.type
            gson.fromJson(it, type)
        } ?: emptyList() // If DB value is NULL, return an empty list
    }
}

object CrewMemberListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCrewMemberList(value: List<CrewMemberLocal>?): String? {
        return value?.takeIf { it.isNotEmpty() }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCrewMemberList(value: String?): List<CrewMemberLocal> { // Return non-nullable List
        return value?.let {
            val type = object : TypeToken<List<CrewMemberLocal>>() {}.type
            gson.fromJson(it, type)
        } ?: emptyList() // If DB value is NULL, return an empty list
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

data class CastMemberLocal(
    val personId: Int,
    val name: String,
    val character: String,
    val profilePath: String?,
)

data class CrewMemberLocal(
    val personId: Int,
    val name: String,
    val profilePath: String?,
    val department: String,
    val job: String,
)
