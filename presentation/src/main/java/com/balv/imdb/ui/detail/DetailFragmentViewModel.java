package com.balv.imdb.ui.detail;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.domain.repositories.IMovieRepository;
import com.balv.imdb.domain.usecases.GetDetailFromNetwork;
import com.balv.imdb.domain.usecases.GetMovieDetailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class DetailFragmentViewModel extends ViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<Movie> getMovieData = new MutableLiveData<>();


    @Inject
    GetMovieDetailUseCase getMovieDetailUseCase;
    @Inject
    GetDetailFromNetwork getDetailFromNetwork;

    @Inject
    public DetailFragmentViewModel() {

    }

    private final MutableLiveData<ApiResult> apiResult = new MutableLiveData<>();

    public MutableLiveData<ApiResult> getErrorLiveData() {
        return apiResult;
    }

    public LiveData<Movie> getMovieDetail(String id) {
        return getMovieData;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public void setup(String imdbId) {
        disposable.add(getMovieDetailUseCase.execute(imdbId)
                .subscribeOn(Schedulers.io())
                .map(movie -> {
                    if (TextUtils.isEmpty(movie.getImdbRated())) {
                        disposable.add(getDetailFromNetwork.execute(imdbId)
                                .subscribeOn(Schedulers.io())
                                .subscribe(apiResult::postValue));
                    }
                    return movie;
                })
                .subscribe(getMovieData::postValue));
    }
}
