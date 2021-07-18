package com.balv.imdb.ui.home.listview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.balv.imdb.R;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.ui.home.listener.IHomeItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.FragmentScoped;

@FragmentScoped
public class HomePagingAdapter extends PagingDataAdapter<Movie, HomePagingAdapter.ItemViewHolder> {
    private static final String TAG = "HomeAdapter";
    private List<Movie> mMovieList = new ArrayList<>();
    @Inject
    public IHomeItemClickListener mClickListener;

    @Inject
    public HomePagingAdapter(){
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return TextUtils.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return TextUtils.equals(oldItem.getImdbRated(), newItem.getImdbRated());
        }
    };


    public void setDataList(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomePagingAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomePagingAdapter.ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;
        TextView rating;
        TextView date;
        ImageView thumb;

        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.movie_title);
            rating = view.findViewById(R.id.imdb_rating);
            date = view.findViewById(R.id.release_date);
            thumb = view.findViewById(R.id.thumb);
        }

        public void bind(Movie data){
            if (data != null) {
                view.setOnClickListener(view -> mClickListener.onItemClick(data.getId()));
                title.setText(data.getTitle());
                rating.setText(data.getImdbRated());
                date.setText(data.getReleased());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                Glide
                        .with(view.getContext())
                        .load(data.getPoster())
                        .centerCrop()
                        .placeholder(R.drawable.mv_place_holder)
                        .apply(requestOptions)
                        .into(thumb);
            }
        }
    }
}
