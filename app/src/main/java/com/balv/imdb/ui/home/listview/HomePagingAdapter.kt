package com.balv.imdb.ui.home.listview

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.balv.imdb.R
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.ui.home.listener.IHomeItemClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class HomePagingAdapter @Inject constructor() :
    PagingDataAdapter<Movie, HomePagingAdapter.ItemViewHolder>(
        DIFF_CALLBACK
    ) {
    private var mMovieList: List<Movie> = ArrayList()

    @JvmField
    @Inject
    var mClickListener: IHomeItemClickListener? = null

    fun setDataList(movieList: List<Movie>) {
        mMovieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.movie_title)
        var rating: TextView = view.findViewById(R.id.imdb_rating)
        var date: TextView = view.findViewById(R.id.release_date)
        var thumb: ImageView =
            view.findViewById(R.id.thumb)

        fun bind(data: Movie?) {
            if (data != null) {
                view.setOnClickListener { view: View? ->
                    mClickListener!!.onItemClick(
                        data.id
                    )
                }
                title.text = data.title
                rating.text = data.voteAverage.toString()
                date.text = data.releaseDate
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
                Glide
                    .with(view.context)
                    .load(data.posterPath)
                    .centerCrop()
                    .placeholder(R.drawable.mv_place_holder)
                    .apply(requestOptions)
                    .into(thumb)
            }
        }
    }

    companion object {
        private const val TAG = "HomeAdapter"
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.voteAverage == newItem.voteAverage
                }
            }
    }
}
