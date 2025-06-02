package com.balv.imdb.domain.models

open class ApiResult<T> {
    var isSuccess: Boolean = false
    private set
    var data: T? = null
        private set
    var error: ErrorResult? = null
        private set

    private constructor(result: T) {
        this.isSuccess = true
        this.data = result
    }

    private constructor(error: ErrorResult?) {
        this.isSuccess = false
        this.error = error
    }

    companion object {
        fun <T> success(result: T): ApiResult<T> {
            return ApiResult(result = result).apply { isSuccess = true }
        }

        fun <T> error(error: ErrorResult?): ApiResult<T> {
            return ApiResult(error = error)
        }
    }
}
