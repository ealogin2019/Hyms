package com.hyms.feature.listing.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hyms.feature.listing.ui.viewmodel.DraftListingViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private enum class PickerType { Gender, Category, Condition }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftListingBasicsScreen(
    modifier: Modifier = Modifier,
    viewModel: DraftListingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var activeSheet by remember { mutableStateOf<PickerType?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (activeSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { activeSheet = null },
            sheetState = sheetState
        ) {
            when (activeSheet) {
                PickerType.Gender -> SimplePicker(
                    title = "Gender",
                    options = listOf("Women", "Men", "Unisex"),
                    selected = state?.draft?.genderSegment,
                    onSelect = { viewModel.onGenderChanged(it); activeSheet = null }
                )
                PickerType.Category -> SimplePicker(
                    title = "Category",
                    options = listOf("Tops", "Bottoms", "Outerwear", "Dresses", "Shoes", "Accessories"),
                    selected = state?.draft?.category,
                    onSelect = { viewModel.onCategoryChanged(it); activeSheet = null }
                )
                PickerType.Condition -> SimplePicker(
                    title = "Condition",
                    options = listOf("New", "Like New", "Good", "Fair"),
                    selected = state?.draft?.condition,
                    onSelect = { viewModel.onConditionChanged(it); activeSheet = null }
                )
                null -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Create listing",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Keep it simple. You can add more details later.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Divider()

        OutlinedTextField(
            value = state?.draft?.title ?: "",
            onValueChange = viewModel::onTitleChanged,
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        PickerRow(
            label = "Gender",
            value = state?.draft?.genderSegment ?: "Select",
            onClick = { activeSheet = PickerType.Gender }
        )

        PickerRow(
            label = "Category",
            value = state?.draft?.category ?: "Select",
            onClick = { activeSheet = PickerType.Category }
        )

        OutlinedTextField(
            value = state?.draft?.sizeLabel ?: "",
            onValueChange = { viewModel.onSizeChanged(it.ifBlank { null }) },
            label = { Text("Size") },
            placeholder = { Text("e.g. UK 10, M, 32\"") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state?.priceText ?: "",
            onValueChange = viewModel::onPriceTextChanged,
            label = { Text("Price") },
            prefix = { Text("£") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        PickerRow(
            label = "Condition",
            value = state?.draft?.condition ?: "Select",
            onClick = { activeSheet = PickerType.Condition }
        )

        NegotiableRow(
            checked = state?.draft?.negotiable ?: false,
            onCheckedChange = viewModel::onNegotiableChanged
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Saved automatically",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PickerRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun NegotiableRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Negotiable", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SimplePicker(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        options.forEach { option ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option) }
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (selected == option) {
                    Text(
                        text = "Selected",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Divider()
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
