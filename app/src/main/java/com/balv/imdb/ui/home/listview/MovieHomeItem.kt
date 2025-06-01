package com.balv.imdb.ui.home.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.balv.imdb.R
import com.balv.imdb.domain.models.Movie

const val tmdbRootPosterPath = "https://image.tmdb.org/t/p/w185"
@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(180.dp)
                .clickable { onClick() }
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = tmdbRootPosterPath + movie.posterPath),
                contentDescription = movie.title,
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = Color(0xFFFFC83D),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movie.voteAverage.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFFC83D)
                )
            }

            Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
    }
}

@Preview
@Composable
fun MovieItemPreview() {
    MovieItem(
        movie = Movie(
            id = 1,
            title = "Exterritorial",
            overview = "When her son vanishes inside a US consulate, ex-special forces soldier Sara does everything in her power to find him — and uncovers a dark conspiracy.",
            popularity = 134.5884,
            posterPath = "/jM2uqCZNKbiyStyzXOERpMqAbdx.jpg",
            releaseDate = "2025-04-29",
            originalTitle = "Exterritorial",
            voteAverage = 6.7856,
            voteCount = 10,
            adult = false,
            backdropPath = "/14UFWFJsGeInCbhTiehRLTff4Yx.jpg",
            genreIds = listOf(1, 2, 3, 4),
            originalLanguage = "en",
            video = false

        ), onClick = {})
}
