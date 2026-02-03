package com.hyms.feature.listing.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private enum class SellStep { Photos, Basics, Publish }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellFlowScreen(
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(SellStep.Photos) } // set Photos later when ready

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create listing") }
            )
        },
        bottomBar = {
            SellFlowBottomBar(
                step = step,
                onBack = {
                    step = when (step) {
                        SellStep.Photos -> SellStep.Photos
                        SellStep.Basics -> SellStep.Photos
                        SellStep.Publish -> SellStep.Basics
                    }
                },
                onNext = {
                    step = when (step) {
                        SellStep.Photos -> SellStep.Basics
                        SellStep.Basics -> SellStep.Publish
                        SellStep.Publish -> SellStep.Publish
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            StepIndicator(step)

            when (step) {
                SellStep.Photos -> PhotosStepScreen() // Replaced PhotosStepPlaceholder()
                SellStep.Basics -> DraftListingBasicsScreen()
                SellStep.Publish -> PublishStepPlaceholder()
            }
        }
    }
}

@Composable
private fun StepIndicator(step: SellStep) {
    val steps = listOf("Photos", "Basics", "Publish")
    val active = when (step) {
        SellStep.Photos -> 0
        SellStep.Basics -> 1
        SellStep.Publish -> 2
    }

    LinearProgressIndicator(
        progress = (active + 1) / steps.size.toFloat(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )

    Text(
        text = "${steps[active]}",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun SellFlowBottomBar(
    step: SellStep,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val backEnabled = step != SellStep.Photos
    val nextLabel = when (step) {
        SellStep.Photos -> "Continue"
        SellStep.Basics -> "Continue"
        SellStep.Publish -> "Publish"
    }

    Surface(tonalElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                enabled = backEnabled,
                modifier = Modifier.weight(1f)
            ) { Text("Back") }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) { Text(nextLabel) }
        }
    }
}

@Composable
private fun PublishStepPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Ready to publish", style = MaterialTheme.typography.titleLarge)
        Text(
            "We’ll connect publishing to the backend next.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}