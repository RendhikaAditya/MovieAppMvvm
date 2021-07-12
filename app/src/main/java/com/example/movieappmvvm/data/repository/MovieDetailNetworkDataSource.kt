package com.example.movieappmvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieappmvvm.data.api.TheMovieDbInterface
import com.example.movieappmvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailNetworkDataSource
    (private val apiService : TheMovieDbInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkstate = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
    get() = _networkstate

    private val _downloadMovieDetailResponse = MutableLiveData<MovieDetails>()
    val downloadMovieResponse: LiveData<MovieDetails>
    get() = _downloadMovieDetailResponse

    fun fetchMovieDetail(movieId : Int){

        _networkstate.postValue(NetworkState.LOADING)

        try{
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadMovieDetailResponse.postValue(it)
                            _networkstate.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkstate.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailDataSourcce", it.message.toString())
                        }
                    )
            )
        }catch (e : Exception){
            Log.e("MovieDetailDaaSource", e.message.toString())
        }
    }


}
