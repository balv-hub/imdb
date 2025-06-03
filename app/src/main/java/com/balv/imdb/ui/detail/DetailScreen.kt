package com.balv.imdb.ui.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.balv.imdb.R
import com.balv.imdb.domain.models.MovieDetail
import com.balv.imdb.ui.home.listview.tmdbRootPosterPath
import com.balv.imdb.ui.icons.ErrorOutlined
import com.balv.imdb.ui.icons.LoadingIndicator
import com.balv.imdb.ui.icons.SentimentVeryDissatisfied

const val tmdbRootBackDropPath = "https://image.tmdb.org/t/p/w500"


@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val movie by viewModel.getMovieData.observeAsState()
    val isLoading by viewModel.loadingLiveData.observeAsState(initial = true)
    val error by viewModel.errorLiveData.observeAsState()

    when {
        isLoading -> {
            LoadingIndicator()
        }

        error != null -> {
            ErrorDisplay(message = error!!, onRetry = {

            })
        }

        movie != null -> {
            FullMovieDetailContent(
                movie = movie!!,
                onBackClicked = onBackClicked,
            )
        }

        else -> {
            MovieNotFoundMessage(onBackClicked = onBackClicked)
        }
    }

}

@Composable
fun FullMovieDetailContent(
    movie: MovieDetail,
    onBackClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val headerHeightPx = with(LocalDensity.current) { 300.dp.toPx() } // Initial height of header
    val toolbarHeightPx = with(LocalDensity.current) { 56.dp.toPx() } // Standard toolbar height

    // Value from 0f (collapsed) to 1f (expanded)
    val headerAlpha by remember {
        derivedStateOf {
            val offset = scrollState.value.toFloat()
            (1f - (offset / (headerHeightPx - toolbarHeightPx * 1.5f))).coerceIn(0f, 1f)
        }
    }
    // Value for parallax effect
    val backdropTranslationY by remember {
        derivedStateOf {
            scrollState.value * 0.4f // Parallax factor
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Header Placeholder (takes up space, content is drawn in the Box above)
            Spacer(modifier = Modifier.height(300.dp)) // Same as initial headerHeight

            // 2. Action Buttons
            ActionButtonsRow(
                onWatchTrailer = { /*TODO*/ },
            )

            // 3. Overview
            OverviewSection(movie.overview)

            DetailsSection(movie)

            CastSection(movie.castMembers)

            CrewSection(movie.crewMembers)


            Spacer(modifier = Modifier.height(16.dp)) // Padding at the bottom
        }

        // Collapsing/Parallax Header Content (Drawn on top of the Spacer in Column)
        MovieDetailHeader(
            movie = movie,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Initial height
                .graphicsLayer {
                    translationY = -scrollState.value.toFloat() // Scrolls with content
                    alpha = 1f // Header itself doesn't fade, only specific elements within
                },
            backdropTranslationY = backdropTranslationY, // Pass parallax translation
            contentAlpha = headerAlpha // Pass alpha for header content elements
        )

        // Top "Toolbar" area (Back button and collapsed title)
        MovieDetailTopBar(
            movieTitle = movie.title,
            onBackClicked = onBackClicked,
            scrollOffset = scrollState.value.toFloat(),
            threshold = headerHeightPx - toolbarHeightPx * 2 // When to show title
        )
    }
}

@Composable
fun MovieDetailTopBar(
    movieTitle: String,
    onBackClicked: () -> Unit,
    scrollOffset: Float,
    threshold: Float
) {
    val showTitle by remember { derivedStateOf { scrollOffset > threshold } }
    val titleAlpha by animateFloatAsState(
        targetValue = if (showTitle) 1f else 0f,
        label = "titleAlpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Standard toolbar height
            .background(
                MaterialTheme.colorScheme.surface.copy(
                    alpha = if (showTitle) 0.9f else 0f // Fade in background
                )
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = if (showTitle) MaterialTheme.colorScheme.onSurface else Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = movieTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = titleAlpha),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun MovieDetailHeader(
    movie: MovieDetail,
    modifier: Modifier = Modifier,
    backdropTranslationY: Float,
    contentAlpha: Float
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = tmdbRootBackDropPath + movie.backdropPath,
            placeholder = painterResource(id = R.drawable.tmvdb_bg), // Replace with actual placeholder
            error = painterResource(id = R.drawable.tmvdb_bg), // Replace
            contentDescription = "Movie Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = backdropTranslationY // Apply parallax
                }
        )

        // Gradient scrim for text readability on backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Main Header Content (Poster, Title, Info)
        Column(
            modifier = Modifier.fillMaxSize(), // Fill the Box
            verticalArrangement = Arrangement.Bottom // Push children to the bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                    )
                    .alpha(contentAlpha)
            ) {
                // Movie Poster
                AsyncImage(
                    model = tmdbRootPosterPath + movie.posterPath,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background), // Replace
                    error = painterResource(id = R.drawable.ic_launcher_background), // Replace
                    contentDescription = movie.title + " Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Bottom // Align items to bottom of this column
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!movie.tagline.isNullOrBlank()) { // Check if tagline exists and is not just whitespace
                        Spacer(modifier = Modifier.height(4.dp)) // Small space above tagline
                        Text(
                            text = movie.tagline!!,
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic), // Example style
                            color = Color.White.copy(alpha = 0.85f), // Slightly less prominent than title
                            maxLines = 2, // Allow tagline to wrap up to 2 lines
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val displayYear = remember(movie.releaseDate) {
                            movie.releaseDate.split("-").firstOrNull()?.takeIf { it.length == 4 } ?: "N/A"
                        }
                        InfoChip(text = displayYear)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        UserScoreIndicator(score = movie.voteAverage.toFloat())
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "User Score",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    GenreChips(genres = movie.genres.map { it.name })

                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String, modifier: Modifier = Modifier) {
    if (text.isNotBlank()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.9f),
            modifier = modifier
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun UserScoreIndicator(score: Float, modifier: Modifier = Modifier) {
    Box(modifier.size(36.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { score / 10f },
            modifier = Modifier.fillMaxSize(),
            color = if (score >= 7) Color(0xFF4CAF50) else if (score >= 4) Color(0xFFFFC107) else Color(
                0xFFF44336
            ),
            strokeWidth = 3.dp,
            trackColor = Color.White.copy(alpha = 0.3f)
        )
        Text(
            text = "%.1f".format(score),
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
fun GenreChips(genres: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        genres.take(4).forEach { genre -> // Take a few to prevent overflow
            Text(
                text = genre,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
fun ActionButtonsRow(
    onWatchTrailer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(onClick = onWatchTrailer, modifier = Modifier.weight(1f)) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Play Trailer",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Watch Trailer")
        }
    }
}

@Composable
fun OverviewSection(overview: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(overview, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp, color = Color.LightGray)
    }
}

@Composable
fun DetailsSection(details: MovieDetail) {
    val budget = remember(details.budget) {
        formatUsdShort(details.budget)
    }

    val revenue = remember(details.revenue) {
        formatUsdShort(details.revenue)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        DetailItem("Original Title:", details.originalTitle)
        DetailItem("Status:", details.status)
        DetailItem("Original Language:", details.originalLanguage)
        DetailItem("Budget:", budget)
        DetailItem("Revenue:", revenue)
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    if (value.isNotBlank()) {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray,
                modifier = Modifier.width(120.dp)
            )
            Text(value, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
        }
    }
}
