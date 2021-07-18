package com.balv.imdb.ui.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.balv.imdb.R;
import com.balv.imdb.databinding.DetailFragmentBinding;
import com.balv.imdb.domain.models.Movie;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailFragment extends Fragment {
    public DetailFragmentViewModel mViewModel;
    private DetailFragmentBinding mBinding;
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DetailFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAlertDialog = new MaterialAlertDialogBuilder(getContext()).setTitle("Error").create();

        mViewModel = new ViewModelProvider(this).get(DetailFragmentViewModel.class);
        String movieId = DetailFragmentArgs.fromBundle(getArguments()).getMovieId();
        mViewModel.setup(movieId);
        mViewModel.getMovieDetail(movieId).observe(getViewLifecycleOwner(), movie -> bind(movie));
        mViewModel.getErrorLiveData().observe(
                getViewLifecycleOwner(),
                apiResult -> {
                    if (!apiResult.isSuccess()) {
                        mAlertDialog.setMessage("Something happened, check API key or network condition");
                        mAlertDialog.show();
                    }
                });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false, false);
    }

    @Override
    public void onDestroy() {
        mAlertDialog.dismiss();
        mAlertDialog = null;
        super.onDestroy();
    }

    private void bind(Movie movie) {
        mBinding.title.setText(movie.getTitle());
        mBinding.year.setText(movie.getReleased());
        mBinding.actors.setText(movie.getActors());
        mBinding.director.setText(movie.getDirector());
        mBinding.genre.setText(movie.getGenre());
        mBinding.time.setText(movie.getTime());
        mBinding.writer.setText(movie.getWriter());
        mBinding.imdbRating.setText(movie.getImdbRated());

        mBinding.year.setText(movie.getReleased());
        mBinding.plot.setText(movie.getPlot());
        Glide.with(getContext())
                .load(movie.getPoster())
                .centerCrop()
                .placeholder(R.drawable.mv_place_holder)
                .into(mBinding.poster);
        updateLoadingIndicator(TextUtils.isEmpty(movie.getImdbRated()));
    }

    private void updateLoadingIndicator(boolean show) {
        mBinding.indeterminateBar.setVisibility(show? View.VISIBLE: View.GONE);
    }

}
