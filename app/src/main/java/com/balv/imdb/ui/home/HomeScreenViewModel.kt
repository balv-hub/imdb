package com.balv.imdb.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.usecases.GetMovieListInput
import com.balv.imdb.domain.usecases.GetMovieListUseCase
import com.balv.imdb.domain.usecases.GetNextMoviePageUseCase
import com.balv.imdb.domain.usecases.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    val refreshing = MutableLiveData(false)

    val errorLiveData: MutableLiveData<ApiResult<*>> = MutableLiveData()
    private val _allMoviesFlow: MutableStateFlow<PagingData<Movie>> =
        MutableStateFlow(PagingData.empty())
    val allMoviesFlow: StateFlow<PagingData<Movie>> = _allMoviesFlow
    private val _popularMoviesFlow: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val popularMoviesFlow: StateFlow<List<Movie>> = _popularMoviesFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getMovieListUseCase.execute(GetMovieListInput(false)).cachedIn(viewModelScope).stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000L),
                    initialValue = PagingData.empty()
                ).collectLatest {
                Log.i("TAG", "movie home updated..... $it")
                    _allMoviesFlow.emit(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getPopularMoviesUseCase.execute(Unit).collect {
                _popularMoviesFlow.emit(it)
            }
        }
    }
}
