package com.example.movieappmvvm.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.movieappmvvm.data.vo.Movie
import androidx.paging.DataSource
import com.example.movieappmvvm.data.api.TheMovieDbInterface
import com.oxcoding.moviemvvm.data.repository.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: TheMovieDbInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {
    val moviLiveDataSource=MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        moviLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}