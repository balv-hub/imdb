package com.balv.imdb.domain.usecases;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.balv.imdb.domain.models.Movie;

import javax.inject.Inject;

@SuppressLint("CheckResult")
public class GetMovieDetailUseCase extends BaseUseCase {
    @Inject
    public GetMovieDetailUseCase(){}

    public LiveData<Movie> getMovieDetail(String id) {
        Log.i("TAG", "getMovieDetail: " + id);
        mMovieRepo.getDetailFromNetwork(id);
        return mMovieRepo.getMovieDetail(id);
    }
}
