package com.example.movieappmvvm.ui.singleMovieDetail

import androidx.lifecycle.LiveData
import com.example.movieappmvvm.data.api.TheMovieDbInterface
import com.example.movieappmvvm.data.repository.MovieDetailNetworkDataSource
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiService: TheMovieDbInterface) {
    lateinit var movieDetailNetworkDataSource: MovieDetailNetworkDataSource

    fun fetchSingleMovieDetail(compositeDisposable: CompositeDisposable, movieId:Int): LiveData<MovieDetails>{
        movieDetailNetworkDataSource = MovieDetailNetworkDataSource(apiService, compositeDisposable)
        movieDetailNetworkDataSource.fetchMovieDetail(movieId)

        return movieDetailNetworkDataSource.downloadMovieResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState>{
        return movieDetailNetworkDataSource.networkState
    }
}