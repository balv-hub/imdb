package com.balv.imdb.data.model.dto


import com.google.gson.annotations.SerializedName

data class MovieCreditsResponseRemote(
    @SerializedName("id") val movieId: Int?, // The ID of the movie these credits belong to
    @SerializedName("cast") val cast: List<CastMemberRemote>?,
    @SerializedName("crew") val crew: List<CrewMemberRemote>?
)

// Cast Member DTO (as defined in your JSON)
data class CastMemberRemote(
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("gender") val gender: Int?, // 0: Not set, 1: Female, 2: Male, 3: Non-binary
    @SerializedName("id") val personId: Int?, // This is the unique ID for the person (actor)
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("profile_path") val profilePath: String?, // Relative image path
    @SerializedName("cast_id") val castIdInMovie: Int?, // ID specific to this movie's casting
    @SerializedName("character") val character: String?,
    @SerializedName("credit_id") val creditId: String?, // Unique ID for this credit entry
    @SerializedName("order") val order: Int? // Order of appearance/billing
)

// Crew Member DTO (as defined in your JSON)
data class CrewMemberRemote(
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("id") val personId: Int?, // This is the unique ID for the person (crew member)
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("profile_path") val profilePath: String?, // Relative image path
    @SerializedName("credit_id") val creditId: String?, // Unique ID for this credit entry
    @SerializedName("department") val department: String?,
    @SerializedName("job") val job: String?
)