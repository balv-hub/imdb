package com.balv.imdb.ui.favorite // Or your relevant UI package

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class) // For Scaffold and TopAppBar
@Composable
fun FavoriteScreen(
    onNavigateBack: (() -> Unit)? = null // Made onNavigateBack nullable for flexibility
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Favorites") },
                navigationIcon = {
                    // Show back button only if onNavigateBack is provided
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(16.dp), // Additional padding for content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This is the Favorites Screen.",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Favorite movies will be listed here.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Favorite Screen")
@Composable
fun FavoriteScreenPreview() {
    MaterialTheme { // Wrap in MaterialTheme for preview
        FavoriteScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, name = "Favorite Screen No Back Button")
@Composable
fun FavoriteScreenNoBackPreview() {
    MaterialTheme {
        FavoriteScreen(onNavigateBack = null) // Preview without the back button
    }
}