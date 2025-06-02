package com.balv.imdb.ui.home.listview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .defaultMinSize(minHeight = 140.dp)
            .width(100.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.defaultMinSize(minHeight = 120.dp, minWidth = 70.dp)
            .fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(model = tmdbRootPosterPath + movie.posterPath),
                contentDescription = movie.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .matchParentSize()
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating badge
                Box(
                    modifier = Modifier
                        .background(Color(0xAA333333), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = null,
                            tint = Color(0xFFFFC83D),
                            modifier = Modifier.size(8.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text =  String.format(Locale.current.platformLocale, "%.1f", movie.voteAverage),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }

        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
