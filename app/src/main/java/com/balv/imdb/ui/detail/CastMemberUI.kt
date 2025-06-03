package com.balv.imdb.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.balv.imdb.R
import com.balv.imdb.domain.models.DomainCastMember
import com.balv.imdb.ui.home.listview.tmdbRootPosterPath

@Composable
fun CastSection(
    cast: List<DomainCastMember>,
    onCastMemberClicked: (personId: Int) -> Unit = {} // Optional: Click listener
) {
    if (cast.isEmpty()) {
        return
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Top Billed Cast",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Spacing between cast items
        ) {
            items(
                items = cast, // Display all passed cast members, or use .take(N)
                key = { member -> member.personId } // Use personId as the stable key
            ) { member ->
                CastItem(
                    member = member,
                    onClicked = { onCastMemberClicked(member.personId) }
                )
            }
        }
    }
}

@Composable
fun CastItem(
    member: DomainCastMember,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp) // Fixed width for each item
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = if (member.profilePath != null) tmdbRootPosterPath + member.profilePath else null,
            placeholder = painterResource(id = R.drawable.user_holder), // Create this drawable
            contentDescription = member.name + " profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape) // Make the image circular
                .background(MaterialTheme.colorScheme.surfaceVariant) // Background for placeholder
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = member.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            maxLines = 2, // Allow name to wrap if long
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = member.character,
            style = MaterialTheme.typography.labelSmall, // Smaller for character name
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Slightly muted color
            maxLines = 2, // Allow character name to wrap
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}