package com.balv.imdb.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.balv.imdb.ui.icons.ErrorOutlined
import com.balv.imdb.ui.icons.SentimentVeryDissatisfied

fun formatUsdShort(value: Long): String {
    if (value <= 0) return "N/A"

    return when {
        value >= 1_000_000_000 -> { // Billions
            val billions = value / 1_000_000_000.0
            if (billions % 1 == 0.0) {
                "$${billions.toLong()}B"
            } else {
                "$${String.format("%.1f", billions)}B"
            }
        }

        value >= 1_000_000 -> { // Millions
            val millions = value / 1_000_000.0
            if (millions % 1 == 0.0) {
                "$${millions.toLong()}M"
            } else {
                "$${String.format("%.1f", millions)}M"
            }
        }

        value >= 1_000 -> { // Thousands
            val thousands = value / 1_000.0
            if (thousands % 1 == 0.0) {
                "$${thousands.toLong()}K"
            } else {
                "$${String.format("%.1f", thousands)}K"
            }
        }

        else -> "$$value" // Less than 1000
    }
}

@Composable
fun ErrorDisplay(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ErrorOutlined,
            contentDescription = "Error Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Something went wrong!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieNotFoundMessage(modifier: Modifier = Modifier, onBackClicked: () -> Unit) {
    Scaffold( // Optional: Use Scaffold for a top app bar with a back button
        topBar = {
            TopAppBar(
                title = { Text("Movie Not Found") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(16.dp), // Additional padding for content
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                // Choose an icon that fits the "not found" or "oops" theme
                imageVector = SentimentVeryDissatisfied, // Or Icons.Filled.BrokenImage, Icons.Filled.MovieFilter (with a slash through it if you had one)
                contentDescription = "Movie not found icon",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Oops! We couldn't find that movie.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "It might have been removed, or the ID is incorrect, or network's having problem. Please try searching again or go back.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onBackClicked) {
                Text("Go Back")
            }
        }
    }
}

