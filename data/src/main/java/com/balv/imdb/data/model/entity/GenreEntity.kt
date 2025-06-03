package com.balv.imdb.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)