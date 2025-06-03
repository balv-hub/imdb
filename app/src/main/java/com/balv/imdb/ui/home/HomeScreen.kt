package com.balv.imdb.ui.home

import MovieHorizontalGrid
import SectionTitle
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.balv.imdb.ui.home.listview.MovieItem
import com.balv.imdb.ui.icons.LoadingIndicator

private const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNavigateToDetail: (Int) -> Unit
) {
    val pagingItems = viewModel.allMoviesFlow.collectAsLazyPagingItems()
    val popularItems = viewModel.popularMoviesFlow.collectAsState()
    val errorResult by viewModel.errorLiveData.observeAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()

    val listState = rememberLazyGridState()
    val scrollOffset by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset + listState.firstVisibleItemIndex * 100 }
    }

    val collapsedHeight = 0.dp
    val expandedHeight = 140.dp

    val popularSectionHeight by animateDpAsState(
        targetValue = if (scrollOffset > 50) collapsedHeight else expandedHeight,
        label = "PopularSectionHeight"
    )


    LaunchedEffect(errorResult) {
        if (errorResult != null && !errorResult!!.isSuccess) {
            showErrorDialog = true
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text("Something happened, check API key or network condition") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            })
    }
    val configuration = LocalConfiguration.current

    val screenHeightDp by remember {
        mutableStateOf(configuration.screenHeightDp.dp)
    }

    //screen height minus (top bar, bottom nav, title, padding
    val gridHeight = remember(screenHeightDp) {
        screenHeightDp - 64.dp - 80.dp - 24.dp - 16.dp
    }

    val refreshing = pagingItems.loadState.refresh is LoadState.Loading

    Log.i("TAG", "HomeScreen: $refreshing ")

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            pagingItems.refresh()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        state = pullToRefreshState,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                AnimatedVisibility(visible = popularSectionHeight > 0.dp) {
                    Column {
                        SectionTitle("Popular")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(popularSectionHeight)
                        ) {
                            MovieHorizontalGrid(
                                items = popularItems, onNavigateToDetail = onNavigateToDetail
                            )
                        }
                    }
                }
            }

            //All movies section
            item {
                SectionTitle("All Movies")
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gridHeight)
                ) {
                    LazyVerticalGrid(
                        state = listState,
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pagingItems.itemCount) { index ->
                            pagingItems[index]?.let { movie ->
                                MovieItem(movie = movie) {
                                    onNavigateToDetail(movie.id)
                                }
                            }
                        }

                        pagingItems.apply {
                            when (loadState.append) {
                                is LoadState.Loading -> {
                                    item(span = { GridItemSpan(3) }) {
                                        LoadingIndicator()
                                    }
                                }

                                is LoadState.Error -> {
                                    item(span = { GridItemSpan(3) }) {
                                        Text(
                                            text = "Error loading more...",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                is LoadState.NotLoading -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
