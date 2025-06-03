import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.ui.home.listview.MovieItem
import com.balv.imdb.ui.icons.LoadingIndicator
import com.balv.imdb.ui.icons.LottieLoadingIndicator

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun MovieHorizontalGrid(
    items: State<List<Movie>>,
    onNavigateToDetail: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (items.value.isEmpty()) {
            item {
                LottieLoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        } else {
            items(items.value) { movie ->
                MovieItem(movie = movie) {
                    onNavigateToDetail(movie.id)
                }
            }
        }
    }
}