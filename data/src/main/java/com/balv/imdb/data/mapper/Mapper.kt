package com.balv.imdb.data.mapper

import com.balv.imdb.data.model.BelongsToCollectionRemote
import com.balv.imdb.data.model.Genre
import com.balv.imdb.data.model.GenreEntity
import com.balv.imdb.data.model.GenreLocal
import com.balv.imdb.data.model.GenreRemote
import com.balv.imdb.data.model.MovieDetailEntity
import com.balv.imdb.data.model.MovieDetailRemote
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.ProductionCompanyLocal
import com.balv.imdb.data.model.ProductionCompanyRemote
import com.balv.imdb.data.model.ProductionCountryLocal
import com.balv.imdb.data.model.ProductionCountryRemote
import com.balv.imdb.data.model.RemoteMovie
import com.balv.imdb.data.model.SpokenLanguageLocal
import com.balv.imdb.data.model.SpokenLanguageRemote
import com.balv.imdb.domain.models.DomainCollection
import com.balv.imdb.domain.models.DomainGenre
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.models.MovieDetail


fun MovieEntity.entityToDomain() = Movie(
    id = this.id,
    adult = this.adult,
    backdropPath = this.backdropPath,
    genreIds = this.genreIds,
    originalLanguage = this.originalLanguage,
    originalTitle = this.originalTitle,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.title,
    video = this.video,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    polledDate = this.polledDate
)


fun RemoteMovie.remoteMovieToEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
    )
}


fun MovieDetailRemote.networkToDomain(): MovieDetail {
    return MovieDetail(
        id = this.id ?: 0,
        adult = this.adult ?: false,
        backdropPath = this.backdropPath,
        genres = this.genres?.map { it.toDomain() } ?: emptyList(),
        originalLanguage = this.originalLanguage.orEmpty(),
        originalTitle = this.originalTitle.orEmpty(),
        overview = this.overview.orEmpty(),
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate.orEmpty(),
        title = this.title.orEmpty(),
        video = this.video ?: false,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        refreshedDate = System.currentTimeMillis(),
        tagline = this.tagline,
        releaseYear = this.releaseDate.orEmpty(),
        runtime = this.runtime ?: 0,
        budget = this.budget ?: 0,
        revenue = this.revenue ?: 0,
        status = this.status.orEmpty(),
        homepage = this.homepage,
        imdbId = this.imdbId.orEmpty(),
        collection = this.belongsToCollection?.toDomain(),
        productionCompanyNames = this.productionCompanies?.map { it.name.orEmpty() } ?: emptyList(),
        spokenLanguageNames = this.spokenLanguages?.map { it.name.orEmpty() } ?: emptyList(),
        originCountryNames = this.originCountry?.map { it } ?: emptyList(),
        formattedRuntime = formatRuntime(this.runtime ?: 0),
    )
}

fun BelongsToCollectionRemote.toDomain() = DomainCollection(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    posterPath = this.posterPath.orEmpty(),
    backdropPath = this.backdropPath.orEmpty()
)

fun Genre.remoteGenreToEntity() = GenreEntity(
    id = this.id, name = this.name
)
fun Genre.toDomain() = DomainGenre(
    id = this.id, name = this.name
)

fun GenreRemote.toDomain() = DomainGenre(
    id = this.id?: 0 , name = this.name.orEmpty()
)



fun MovieDetailRemote.toEntity(): MovieDetailEntity? {
    // The movie ID from the remote source is crucial. If it's null, we can't create a valid entity.
    if (this.id == null) return null

    return MovieDetailEntity(
        id = this.id, // Non-nullable
        adult = this.adult ?: false,
        backdropPath = this.backdropPath,
        collectionId = this.belongsToCollection?.id,
        collectionName = this.belongsToCollection?.name,
        collectionPosterPath = this.belongsToCollection?.posterPath,
        collectionBackdropPath = this.belongsToCollection?.backdropPath,
        budget = this.budget ?: 0L,
        homepage = this.homepage,
        imdbId = this.imdbId,
        originCountryList = this.originCountry ?: emptyList(),
        originalLanguage = this.originalLanguage ?: "N/A",
        originalTitle = this.originalTitle ?: "N/A",
        overview = this.overview ?: "No overview available.",
        popularity = this.popularity ?: 0.0,
        posterPath = this.posterPath,
        productionCompanies = this.productionCompanies?.mapNotNull { it.toLocal() } ?: emptyList(),
        productionCountries = this.productionCountries?.mapNotNull { it.toLocal() } ?: emptyList(),
        releaseDate = this.releaseDate ?: "N/A",
        revenue = this.revenue ?: 0L,
        runtime = this.runtime ?: 0,
        spokenLanguages = this.spokenLanguages?.mapNotNull { it.toLocal() } ?: emptyList(),
        status = this.status ?: "Unknown",
        tagline = this.tagline,
        title = this.title ?: "N/A",
        video = this.video ?: false,
        voteAverage = this.voteAverage ?: 0.0,
        voteCount = this.voteCount ?: 0,
        lastRefreshed = System.currentTimeMillis(),
        genres = this.genres?.map { it.toLocal() } ?: emptyList())
}

fun GenreRemote.toLocal() = GenreLocal(
    id = this.id ?: 0, name = this.name ?: ""
)

// --- Mapper for individual nested remote objects to local data classes (for TypeConverters) ---
fun ProductionCompanyRemote.toLocal(): ProductionCompanyLocal? {
    if (this.id == null || this.name == null) return null // Basic validation
    return ProductionCompanyLocal(
        id = this.id, logoPath = this.logoPath, name = this.name, originCountry = this.originCountry
    )
}

fun ProductionCountryRemote.toLocal(): ProductionCountryLocal? {
    if (this.iso31661 == null || this.name == null) return null
    return ProductionCountryLocal(
        iso31661 = this.iso31661, name = this.name
    )
}

fun SpokenLanguageRemote.toLocal(): SpokenLanguageLocal? {
    if (this.iso6391 == null || this.englishName == null || this.name == null) return null
    return SpokenLanguageLocal(
        englishName = this.englishName, iso6391 = this.iso6391, name = this.name
    )
}


// --- Mapper for GenreRemote to GenreEntity ---
fun GenreRemote.toEntity(): GenreEntity? {
    if (this.id == null || this.name == null) return null // ID and Name are primary for GenreEntity
    return GenreEntity(
        id = this.id, name = this.name
    )
}

fun List<GenreRemote>.toEntityList(): List<GenreEntity> {
    return this.mapNotNull { it.toEntity() }
}


// Helper to format runtime (same as before)
private fun formatRuntime(runtimeMinutes: Int): String {
    if (runtimeMinutes <= 0) return "N/A" // Or "" or some other placeholder
    val hours = runtimeMinutes / 60
    val minutes = runtimeMinutes % 60
    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0 || hours == 0) append("${minutes}m") // Show "0m" if runtime is 0 but you want to display something
    }.trim().ifEmpty { "N/A" } // Ensure it's not empty if both hours and minutes are 0 after logic
}

// Helper to extract release year (same as before)
private fun extractReleaseYear(releaseDate: String?): String {
    // Handle cases where releaseDate might be null or not in YYYY-MM-DD format
    return releaseDate?.split("-")?.firstOrNull()?.takeIf { it.length == 4 } ?: "N/A"
}

fun MovieDetailEntity.toDomain(): MovieDetail {
    // Map the embedded GenreLocal list to DomainGenre list
    val domainGenres: List<DomainGenre> = this.genres.map { genreLocal ->
        DomainGenre(id = genreLocal.id, name = genreLocal.name)
    }

    // Create DomainCollection if relevant data exists
    val domainCollection: DomainCollection? =
        if (this.collectionId != null && this.collectionName != null) {
            DomainCollection(
                id = this.collectionId,
                name = this.collectionName,
                posterPath = this.collectionPosterPath,
                backdropPath = this.collectionBackdropPath
            )
        } else {
            null
        }

    val productionCompanyNames: List<String> = this.productionCompanies.map { it.name }

    val spokenLanguageNames: List<String> = this.spokenLanguages.mapNotNull { it.englishName.takeIf { name -> name.isNotBlank() } }


    return MovieDetail(
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        tagline = this.tagline, // Already nullable
        overview = this.overview,
        posterPath = this.posterPath, // Already nullable
        backdropPath = this.backdropPath, // Already nullable
        releaseDate = this.releaseDate, // Assumed non-null from entity, but can be "N/A" if source was bad
        releaseYear = extractReleaseYear(this.releaseDate),
        runtime = this.runtime,
        formattedRuntime = formatRuntime(this.runtime),
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        genres = domainGenres,
        adult = this.adult,
        budget = this.budget,
        revenue = this.revenue,
        status = this.status,
        originalLanguage = this.originalLanguage, // You might want to map language codes (e.g., "en") to full names ("English") here or in ViewModel
        homepage = this.homepage, // Already nullable
        imdbId = this.imdbId.orEmpty(), // Already nullable
        collection = domainCollection,
        productionCompanyNames = productionCompanyNames,
        spokenLanguageNames = spokenLanguageNames,
        originCountryNames = this.originCountryList,
        popularity = this.popularity,
        video = this.video,
        refreshedDate = this.lastRefreshed
    )
}

