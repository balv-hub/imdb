package com.balv.imdb.ui.detail;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.balv.imdb.domain.models.ErrorResult;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.domain.repositories.IMovieRepository;
import com.balv.imdb.domain.usecases.GetMovieDetailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class DetailFragmentViewModel extends ViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IMovieRepository movieRepository;


    @Inject
    GetMovieDetailUseCase getMovieDetailUseCase;

    @Inject
    public DetailFragmentViewModel() {

    }

    private MutableLiveData<ErrorResult> apiError = new MutableLiveData<>();
    public MutableLiveData<ErrorResult> getErrorLiveData(){
        return apiError;
    }

    public LiveData<Movie> getMovieDetail(String id){
        return Transformations.map(movieRepository.getMovieDetail(id), movie ->{
            if (TextUtils.isEmpty(movie.getImdbRated())) {
                disposable.add(movieRepository.getDetailFromNetwork(id).
                        subscribe(apiResult ->{
                            if (!apiResult.isSuccess()) {
                                apiError.postValue(apiResult.getError());
                            }

                }));
            }
            return movie;
        });
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }
}
