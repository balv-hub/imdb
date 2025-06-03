package com.balv.imdb.data.model.dto

import com.google.gson.annotations.SerializedName

data class SearchData(
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<RemoteMovie>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages:Int,
)

