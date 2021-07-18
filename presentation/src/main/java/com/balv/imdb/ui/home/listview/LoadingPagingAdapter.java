package com.balv.imdb.ui.home.listview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.balv.imdb.R;
import com.balv.imdb.databinding.LoadingViewBinding;

public class LoadingPagingAdapter extends LoadStateAdapter<LoadingPagingAdapter.NetworkStateViewHolder> {
    private static final String TAG = "LoadingPagingAdapter";

    @Override
    public void onBindViewHolder(@NonNull NetworkStateViewHolder networkStateViewHolder, @NonNull LoadState loadState) {
        Log.i(TAG, "onBindViewHolder: " + loadState);
        networkStateViewHolder.bind(loadState);
    }

    @NonNull
    @Override
    public NetworkStateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        Log.i(TAG, "onCreateViewHolder: ");
        LoadingViewBinding binding = LoadingViewBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new NetworkStateViewHolder(binding);
    }

    public class NetworkStateViewHolder extends RecyclerView.ViewHolder {
        LoadingViewBinding binding;
        public NetworkStateViewHolder(@NonNull LoadingViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LoadState loadState) {
            binding.progressBar.setVisibility(loadState instanceof LoadState.Loading? View.VISIBLE : View.GONE);
            binding.errorMsg.setVisibility(loadState instanceof LoadState.Error? View.VISIBLE : View.GONE);
            binding.retryButton.setVisibility(loadState instanceof LoadState.Error? View.VISIBLE : View.GONE);
        }
    }
}
