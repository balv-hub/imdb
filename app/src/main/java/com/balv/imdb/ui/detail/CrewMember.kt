package com.balv.imdb.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.balv.imdb.R // Your R file
import com.balv.imdb.domain.models.DomainCrewMember
import com.balv.imdb.ui.home.listview.tmdbRootPosterPath

@Composable
fun CrewSection(
    crew: List<DomainCrewMember>,
    onCrewMemberClicked: (personId: Int) -> Unit = {}
) {
    val displayedCrew = crew
        .distinctBy { it.personId }
        .take(10)

    if (displayedCrew.isEmpty()) {
        return
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Key Crew", // Or "Crew"
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = displayedCrew,
                key = { member -> "${member.personId}-${member.job}" } // Key combining person and job for uniqueness if one person has multiple jobs
            ) { member ->
                CrewItem(
                    member = member,
                    onClicked = { onCrewMemberClicked(member.personId) }
                )
            }
        }
    }
}

@Composable
fun CrewItem(
    member: DomainCrewMember,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp) // Similar width to CastItem for consistency
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = if (member.profilePath != null) tmdbRootPosterPath + member.profilePath else null,
            placeholder = painterResource(id = R.drawable.user_holder),
            contentDescription = member.name + " profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = member.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = member.job, // Display the job/role
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}