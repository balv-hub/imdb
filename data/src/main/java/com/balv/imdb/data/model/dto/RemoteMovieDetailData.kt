package com.balv.imdb.data.model.dto

import com.google.gson.annotations.SerializedName


data class MovieDetailRemote(
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollectionRemote?,
    @SerializedName("budget") val budget: Long?, // Use Long for currency
    @SerializedName("genres") val genres: List<GenreRemote>?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("id") val id: Int?, // Keep as Int, primary key
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("origin_country") val originCountry: List<String>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyRemote>?,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountryRemote>?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("revenue") val revenue: Long?, // Use Long for currency
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguageRemote>?,
    @SerializedName("status") val status: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("video") val video: Boolean?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?
)

// --- Nested Remote DTOs ---
data class BelongsToCollectionRemote(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?
)

data class GenreRemote(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
)

data class ProductionCompanyRemote(
    @SerializedName("id") val id: Int?,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("origin_country") val originCountry: String?
)

data class ProductionCountryRemote(
    @SerializedName("iso_3166_1") val iso31661: String?,
    @SerializedName("name") val name: String?
)

data class SpokenLanguageRemote(
    @SerializedName("english_name") val englishName: String?,
    @SerializedName("iso_639_1") val iso6391: String?,
    @SerializedName("name") val name: String?
)