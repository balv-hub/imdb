package com.balv.imdb.domain.models


data class ErrorResult(val code: Int, val message: String) {
    override fun toString(): String {
        return String.format("ErrorResult(%s): %s", code, message)
    }
}