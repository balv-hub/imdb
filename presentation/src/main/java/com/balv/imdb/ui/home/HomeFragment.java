package com.balv.imdb.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balv.imdb.R;
import com.balv.imdb.databinding.HomeFragmentBinding;
import com.balv.imdb.ui.home.listener.IHomeItemClickListener;
import com.balv.imdb.ui.home.listview.HomePagingAdapter;
import com.balv.imdb.ui.home.listview.LoadingPagingAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.Unit;


@AndroidEntryPoint
public class HomeFragment extends Fragment implements IHomeItemClickListener {
    private static final String TAG = "HomeFragment";
    private NavController mNavController;
    public HomeFragmentViewModel mViewModel;
    private HomeFragmentBinding mBinding;

    @Inject
    public HomePagingAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    private AlertDialog  mAlertDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = NavHostFragment.findNavController(this);
        mViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        mViewModel.setupPaging();

        setupListView();
        mAlertDialog = new MaterialAlertDialogBuilder(getContext()).setTitle("Error").create();

        mViewModel.getErrorLiveData().observe(
                getViewLifecycleOwner(),
                apiResult -> {
                    if (!apiResult.isSuccess()) {
                        mAlertDialog.setMessage("Something happened, check API key or network condition");
                        mAlertDialog.show();
                    }
                });

        mViewModel.getPagingLiveData().observe(getViewLifecycleOwner(), pagingData -> {
            Log.i(TAG, "PagingLiveData: " + pagingData);
            mAdapter.submitData(getLifecycle(), pagingData);
            showMoreIcon(true);
        });

        LoadingPagingAdapter loadStateAdapter = new LoadingPagingAdapter();
        mAdapter.withLoadStateFooter(loadStateAdapter);

        mViewModel.observerMainListData().observe(
                getViewLifecycleOwner(),
                list -> {
                    Log.i(TAG, "onViewCreated: " + list.size());
                    if (list.isEmpty()) {
                        updateLoadingIndicator(true);
                    } else {
                        updateLoadingIndicator(false);
                    }
                }
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true, false);
    }

    @Override
    public void onDestroy() {
        mAlertDialog.dismiss();
        mAlertDialog = null;
        super.onDestroy();
    }

    public void onItemClick(String itemId) {
        Log.i(TAG, "onItemClick: " + itemId);
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(itemId);
        mNavController.navigate(action);
    }

    private void setupListView(){
        mBinding.listView.setAdapter(mAdapter);
        mBinding.listView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mViewModel.getGetNextMoviePage();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    showMoreIcon(false);
                }
            }
        });
    }

    private void updateLoadingIndicator(boolean show) {
        mBinding.loadingIndicator.setVisibility(show? View.VISIBLE: View.GONE);
    }

    private void showMoreIcon(boolean show){
        mBinding.more.setVisibility(show? View.VISIBLE: View.GONE);
    }
}
