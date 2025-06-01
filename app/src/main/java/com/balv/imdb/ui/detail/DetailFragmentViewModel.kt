package com.balv.imdb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.usecases.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val getMovieData = MutableLiveData<Movie>()

    val errorLiveData: MutableLiveData<ApiResult<*>> = MutableLiveData()

    suspend fun getMovieDetail(id: Int): Flow<Movie?> {
        return getMovieDetailUseCase.execute(id)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
