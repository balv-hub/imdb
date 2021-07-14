package com.balv.imdb.data.model;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.ErrorResult;

public class MovieDetailResult extends ApiResult<MovieData> {
    public MovieDetailResult(MovieData result) {
        super(result);
    }

    public MovieDetailResult(ErrorResult error) {
        super(error);
    }
}
