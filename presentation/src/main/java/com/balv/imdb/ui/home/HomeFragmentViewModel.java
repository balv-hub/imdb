package com.balv.imdb.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.domain.repositories.IMovieRepository;
import com.balv.imdb.domain.usecases.GetMovieDetailUseCase;
import com.balv.imdb.domain.usecases.GetMovieListUseCase;
import com.balv.imdb.domain.usecases.GetNextMoviePageUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

@HiltViewModel
public class HomeFragmentViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    IMovieRepository movieRepository;

    @Inject
    GetNextMoviePageUseCase getNextMoviePageUseCase;

    @Inject
    GetMovieListUseCase getMovieListUseCase;



    private final MutableLiveData<ApiResult> apiError = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> listMovieData = new MutableLiveData<>();
    private final MutableLiveData<PagingData<Movie>> pagingDataLiveData = new MutableLiveData<>();

    public MutableLiveData<ApiResult> getErrorLiveData() {
        return apiError;
    }

    public MutableLiveData<PagingData<Movie>> getPagingLiveData() {
        return pagingDataLiveData;
    }

    private final MutableLiveData<Boolean> showMore = new MutableLiveData<>();


    @Inject
    public HomeFragmentViewModel() {

    }

    @SuppressLint("CheckResult")
    public void getGetNextMoviePage() {
        disposable.add(Single.fromCallable(() -> Math.ceil(movieRepository.getCurrentDataSize() / 10.0) + 1)
                .subscribeOn(Schedulers.io())
                .subscribe(page -> {
                    Log.i("TAG", "getGetNextMoviePage: " + page);
                    disposable.add(
                            getNextMoviePageUseCase.execute((int) Math.round(page))
                                    .subscribe(t -> {
                                                if (t.isSuccess()) {
                                                    showMore.postValue(true);
                                                }
                                                apiError.postValue(t);
                                            }
                                    )
                    );
                }, Throwable::printStackTrace));

    }

    public LiveData<List<Movie>> getListLiveData() {
        return listMovieData;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public void setup() {
        disposable.add(
                movieRepository.getPagingData()
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                pagingDataLiveData::postValue,
                                Throwable::printStackTrace
                        )
        );

        disposable.add(
                getMovieListUseCase.execute(Unit.INSTANCE)
                        .subscribe(listMovieData::postValue, Throwable::printStackTrace)
        );
    }
}
