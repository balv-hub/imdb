package com.balv.imdb.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.models.MovieDetail
import com.balv.imdb.domain.usecases.GetGenresResultUseCase
import com.balv.imdb.domain.usecases.GetMovieCreditUseCase
import com.balv.imdb.domain.usecases.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getGenresUseCase: GetGenresResultUseCase,
    private val getCreditUseCase: GetMovieCreditUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _getMovieData = MutableLiveData<MovieDetail?>()
    val getMovieData: LiveData<MovieDetail?> = _getMovieData

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    private val _loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(true)
    val errorLiveData: LiveData<String> = _errorLiveData
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private val movieId: Int? = savedStateHandle.get<Int>("movieId")

    init {
        Log.i(TAG, "movieId:  $movieId")
        movieId?.let {
            if (it != 0 && it != -1) {
                viewModelScope.launch(Dispatchers.IO) {
                    getMovieDetail(it).collectLatest { mv ->
                        _loadingLiveData.postValue(false)
                        _getMovieData.postValue(mv)
                        if (mv != null && mv.castMembers.isEmpty()) {
                            getCreditUseCase.execute(it)
                        }
                    }
                }
            } else {
                _errorLiveData.postValue("Invalid Movie ID.")
            }
        } ?: run {
            _errorLiveData.postValue("Invalid Movie ID.")
        }
    }

    private suspend fun getMovieDetail(id: Int): Flow<MovieDetail?> {
        return getMovieDetailUseCase.execute(id)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    companion object {
        private const val TAG = "MovieViewModel"
    }
}
