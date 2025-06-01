package com.balv.imdb.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.balv.imdb.ui.home.listview.MovieItem

@Composable
fun HomeScreen(
    viewModel: HomeFragmentViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    val pagingItems = viewModel.pagingLiveData.collectAsLazyPagingItems()
    val errorResult by viewModel.errorLiveData.observeAsState()

    var showErrorDialog by remember { mutableStateOf(false) }

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
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (pagingItems.itemCount == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
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
                            item(span = { GridItemSpan(2) }) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        is LoadState.Error -> {
                            item(span = { GridItemSpan(2) }) {
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
