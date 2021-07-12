package com.example.movieappmvvm.ui.popularMovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappmvvm.R
import com.example.movieappmvvm.data.api.POSTER_URL
import com.example.movieappmvvm.databinding.MovieListItemBinding
import com.example.movieappmvvm.databinding.NetworkStateItemBinding
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.ui.singleMovieDetail.SingleMovie
import com.example.movieappmvvm.data.vo.Movie

class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View
        if (viewType==MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkStatViewHolder).bind(networkState)
        }
    }

    fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(movie: Movie?, context: Context){
            val binding = MovieListItemBinding.bind(itemView)

            binding.cvMovieTitle.text = movie?.title
            binding.cvMovieReleaseDate.text = movie?.releaseDate

            val moviePostURL = POSTER_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePostURL)
                .into(binding.cvIvMoviePoster)

            itemView.setOnClickListener {
                val intent= Intent(context,SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }

    }

    class NetworkStatViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            val binding= NetworkStateItemBinding.bind(itemView)
            if (networkState!=null && networkState == NetworkState.LOADING){
                binding.progressBarItem.visibility = View.VISIBLE
            }else{
                binding.progressBarItem.visibility = View.GONE
            }

            if (networkState!=null && networkState == NetworkState.ERROR){
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            }else if (networkState !=  null && networkState == NetworkState.ENDOFLIST){
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            }else{
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState : NetworkState? = this.networkState
        val hadExtraRow : Boolean = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow : Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount-1)
        }


    }
}