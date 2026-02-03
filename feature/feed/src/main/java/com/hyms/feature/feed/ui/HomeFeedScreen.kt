package com.hyms.feature.feed.ui

import android.util.Log // Added import
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyms.feature.feed.domain.model.FeedItem
import java.text.NumberFormat
import java.util.Locale
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Added this import
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview // For preview of the composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    navToDetail: (String) -> Unit,
    viewModel: HomeFeedViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsStateWithLifecycle(initialValue = emptyList())
    val recent by viewModel.recentViewed.collectAsStateWithLifecycle(emptyList())

    val recentItems = remember(items, recent) {
        recent.mapNotNull { r ->
            items.firstOrNull { it.id == r.listingId }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Hyms") }
        )

        RecentlyViewedRail(
            items = recentItems,
            onClick = { id ->
                viewModel.onItemOpened(id)
                navToDetail(id)
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = { it.id }) { item ->
                ListingCard(item = item, onClick = {
                    viewModel.onItemOpened(item.id)
                    navToDetail(item.id)
                })
            }
        }
    }
}

@Composable
private fun ListingCard(item: FeedItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.78f)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = formatGbp(item.priceMinor),
                style = MaterialTheme.typography.titleMedium
            )

            val meta = listOfNotNull(item.sizeLabel, item.brand).joinToString(" · ")
            if (meta.isNotBlank()) {
                Text(
                    text = meta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RecentlyViewedRail(
    items: List<FeedItem>,
    onClick: (String) -> Unit
) {
    if (items.isEmpty()) return

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Recently viewed",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Added Spacer for visual separation

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items, key = { it.id }) { item ->
                Card(
                    onClick = { onClick(item.id) },
                    modifier = Modifier.width(120.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(0.78f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

private fun formatGbp(priceMinor: Long): String {
    val pounds = priceMinor / 100.0
    val nf = NumberFormat.getCurrencyInstance(Locale.UK)
    return nf.format(pounds)
}