package com.balv.imdb.ui.home.listview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.balv.imdb.databinding.LoadingViewBinding
import com.balv.imdb.ui.home.listview.LoadingPagingAdapter.NetworkStateViewHolder

class LoadingPagingAdapter : LoadStateAdapter<NetworkStateViewHolder>() {
    override fun onBindViewHolder(
        networkStateViewHolder: NetworkStateViewHolder,
        loadState: LoadState
    ) {
        Log.i(TAG, "onBindViewHolder: $loadState")
        networkStateViewHolder.bind(loadState)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        loadState: LoadState
    ): NetworkStateViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val binding =
            LoadingViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return NetworkStateViewHolder(binding)
    }

    inner class NetworkStateViewHolder(var binding: LoadingViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState?) {
            binding.progressBar.visibility =
                if (loadState is Loading) View.VISIBLE else View.GONE
            binding.errorMsg.visibility =
                if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            binding.retryButton.visibility =
                if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val TAG = "LoadingPagingAdapter"
    }
}
