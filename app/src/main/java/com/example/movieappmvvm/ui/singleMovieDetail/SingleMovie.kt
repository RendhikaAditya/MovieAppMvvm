package com.example.movieappmvvm.ui.singleMovieDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movieappmvvm.data.api.POSTER_URL
import com.example.movieappmvvm.data.api.TheMovieDBClient
import com.example.movieappmvvm.data.api.TheMovieDbInterface
import com.example.movieappmvvm.databinding.ActivitySingleMovieBinding
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository
    private lateinit var binding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId: Int  = intent.getIntExtra("id",1)

        val apiService : TheMovieDbInterface = TheMovieDBClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.moviewDetail.observe(this, Observer {
            bindUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if (it==NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUi(it: MovieDetails?) {
        binding.movieTitle.text = it!!.title
        binding.movieTagline.text = it.tagline
        binding.movieReleaseDate.text = it.releaseDate
        binding.movieRating.text = it.rating.toString()
        binding.movieRuntime.text = it.runtime.toString()+" Minutes"
        binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePostURL = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePostURL)
            .into(binding.ivMoviePoster)


    }

    private fun getViewModel(modelId: Int): SingleMovieViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieDetailRepository, modelId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}