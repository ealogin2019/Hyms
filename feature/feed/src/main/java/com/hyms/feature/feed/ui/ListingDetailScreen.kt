package com.hyms.feature.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    id: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listing") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.78f)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Placeholder details (we’ll wire real data next)
            Text(
                text = "Item $id",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = formatGbp(3499), // placeholder £34.99
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Size · Brand",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /* later: message/buy */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Message seller")
            }
        }
    }
}

private fun formatGbp(priceMinor: Long): String {
    val pounds = priceMinor / 100.0
    val nf = NumberFormat.getCurrencyInstance(Locale.UK)
    return nf.format(pounds)
}