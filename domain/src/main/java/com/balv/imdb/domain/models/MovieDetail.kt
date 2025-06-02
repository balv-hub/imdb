package com.balv.imdb.domain.models

// Simplified Genre for the domain layer
data class DomainGenre(
    val id: Int,
    val name: String
)

// Simplified Production Company for the domain layer (if needed)
data class DomainProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String? // Only if you display logos
)

// Main MovieDetail Domain Model
data class MovieDetail(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val tagline: String?, // Can be null if not always present
    val overview: String,
    val posterPath: String?, // Full path can be constructed later or here
    val backdropPath: String?, // Full path can be constructed later or here
    val releaseDate: String, // Or java.time.LocalDate if you parse it
    val releaseYear: String, // Derived for convenience
    val runtime: Int, // In minutes
    val voteAverage: Double?, // e.g., 7.8
    val voteCount: Int?,
    val genres: List<DomainGenre>,
    val adult: Boolean,

    // Optional detailed information, can be nullable or have defaults
    val budget: Long, // Or a formatted string like "$100,000,000"
    val revenue: Long, // Or a formatted string
    val status: String,
    val originalLanguage: String, // e.g., "English"
    val homepage: String?,
    val imdbId: String,
    val popularity: Double?,
    val video: Boolean,
    val formattedRuntime: String?,


    val collection: DomainCollection?, // Simplified collection info

    // Simplified lists if you only need names, or more complex if you need more
    val productionCompanyNames: List<String>,
    // Or if you need more details:
    // val productionCompanies: List<DomainProductionCompany>,

    val spokenLanguageNames: List<String>,
    val originCountryNames: List<String>,
    val refreshedDate: Long = 0
)

// Simplified Collection for the domain layer
data class DomainCollection(
    val id: Int,
    val name: String,
    val posterPath: String?,
    val backdropPath: String?
)