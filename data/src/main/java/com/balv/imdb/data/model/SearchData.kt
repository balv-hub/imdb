package com.balv.imdb.data.model

import com.google.gson.annotations.SerializedName

data class SearchData(
    @SerializedName("Search")
    val list: ArrayList<MovieNetworkObject>? = null,
    val totalResults: String? = null,
    @SerializedName("Response")
    val response: String? = null
)

