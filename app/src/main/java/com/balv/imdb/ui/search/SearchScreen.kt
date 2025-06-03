package com.balv.imdb.ui.search // Or your relevant UI package

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: (() -> Unit)? = null // Optional: For a back button if not part of a NavHost top bar
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Dummy search results for display
    val searchResults by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.length > 2) {
                // Simulate fetching results
                List(10) { index -> "Result for '$searchQuery' #${index + 1}" }
            } else {
                emptyList()
            }
        }
    }

    // Request focus on the search bar when the screen enters
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show() // Optionally show keyboard automatically
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Movies") },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
                // You could also put the SearchBar directly in the TopAppBar for a different style
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                label = { Text("Search for movies, TV shows...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            // Optionally: keyboardController?.show()
                        }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear Search")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // Perform search action
                        // For now, just dismiss keyboard
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        // In a real app, you'd trigger viewModel.search(searchQuery)
                    }
                ),
                singleLine = true,
            )

            // Content Area (Results or Placeholder)
            if (searchQuery.length <= 2 && searchResults.isEmpty()) {
                // Placeholder when search query is short or no results yet
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start typing to find your favorite movies or TV shows!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (searchResults.isEmpty() && searchQuery.length > 2) {
                // Placeholder when search query is long enough but no results found
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No results found for \"$searchQuery\".\nTry a different search term.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else {
                // Display Search Results
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { resultText ->
                        SearchResultItem(text = resultText)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(text: String, modifier: Modifier = Modifier) {
    // Replace this with your actual search result item UI
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, name = "Search Screen Empty")
@Composable
fun SearchScreenEmptyPreview() {
    MaterialTheme {
        SearchScreen()
    }
}

@Preview(showBackground = true, name = "Search Screen With Query")
@Composable
fun SearchScreenWithQueryPreview() {
    // To preview with a query, we'd need to hoist the state or use a more complex preview setup.
    // This simple preview will show the initial empty state.
    // For now, let's simulate by directly calling a state with results.
    MaterialTheme {
        // This is a simplified preview, in a real app the state would be managed.
        Column {
            val searchQuery = "Batman"
            val searchResults = List(5) { "Result for '$searchQuery' #${it + 1}" }

            OutlinedTextField(value = searchQuery, onValueChange = {}, readOnly = true, modifier = Modifier.padding(16.dp).fillMaxWidth())
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { resultText ->
                    SearchResultItem(text = resultText)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Search Screen No Results")
@Composable
fun SearchScreenNoResultsPreview() {
    MaterialTheme {
        Column {
            val searchQuery = "XyzNonExistentMovie123"
            OutlinedTextField(value = searchQuery, onValueChange = {}, readOnly = true, modifier = Modifier.padding(16.dp).fillMaxWidth())
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for \"$searchQuery\".\nTry a different search term.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}