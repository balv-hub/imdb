package com.balv.imdb.data.model.dto

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("Source")
    val source: String? = null,

    @SerializedName("Value")
    val value: String? = null
)
