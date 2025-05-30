package com.balv.imdb.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import com.balv.imdb.domain.usecases.GetMovieListUseCase
import com.balv.imdb.domain.usecases.GetNextMoviePageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getNextMoviePageUseCase: GetNextMoviePageUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
) : ViewModel() {

    val errorLiveData: MutableLiveData<ApiResult<*>> = MutableLiveData()
    val pagingLiveData: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())

    private val showMore = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            getMovieListUseCase.execute(Unit).collect {
                pagingLiveData.emit(it)
            }
        }
    }


//    @get:SuppressLint("CheckResult")
//    val getNextMoviePage: Unit
//        get() {
//            disposable.add(Single.fromCallable {
//                ceil(
//                    movieRepository!!.currentDataSize / 10.0
//                ) + 1
//            }
//                .subscribeOn(Schedulers.io())
//                .subscribe({ page: Double ->
//                    Log.i("TAG", "getGetNextMoviePage: $page")
//                    disposable.add(
//                        getNextMoviePageUseCase!!.execute(Math.round(page).toInt())
//                            .subscribe { t: ApiResult<*> ->
//                                if (t.isSuccess) {
//                                    showMore.postValue(true)
//                                }
//                                errorLiveData.postValue(t)
//                            }
//                    )
//                }, { obj: Throwable -> obj.printStackTrace() }))
//        }

}
