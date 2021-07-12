package com.example.movieappmvvm.ui.singleMovieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val moviewRepository: MovieDetailRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviewDetail: LiveData<MovieDetails>by lazy{
        moviewRepository.fetchSingleMovieDetail(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState>by lazy{
        moviewRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}