package com.example.composedemo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composedemo.domain.model.CurrencyInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCurrencyBottomSheet(
    currentBase: String,
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    // all available currencies from CurrencyInfo
    val allCurrencies = remember { CurrencyInfo.details.keys.sorted() }

    val filtered = remember(searchQuery) {
        if (searchQuery.isBlank()) allCurrencies
        else allCurrencies.filter { code ->
            code.contains(searchQuery, ignoreCase = true) ||
                    CurrencyInfo.getName(code)
                        .contains(searchQuery, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            // title
            Text(
                text = "Select Base Currency",
                style = typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                placeholder = {
                    Text(
                        text = "Search...",
                        style = typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            // list
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(
                    items = filtered,
                    key = { it }
                ) { code ->
                    val isSelected = code == currentBase
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "${CurrencyInfo.getFlag(code)}  $code",
                                style = typography.titleSmall,
                                fontWeight = if (isSelected)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        supportingContent = {
                            Text(
                                text = CurrencyInfo.getName(code),
                                style = typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        },
                        trailingContent = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = colors.primary
                                )
                            }
                        } else null,
                        modifier = Modifier.clickable {
                            onCurrencySelected(code)
                        }
                    )
                    HorizontalDivider(color = colors.outlineVariant)
                }
            }
        }
    }
}
//```
//
//---
//
//## What This Screen Has
//```
//✅ TopAppBar with base currency chip
//✅ Search bar with clear button
//✅ Sort chips with active indicator
//✅ Loading state with spinner and text
//✅ Error state with icon, message, retry button
//✅ Empty search result state
//✅ Success state with LazyColumn
//✅ Scroll to top FAB with animation
//✅ Snackbar on error
//✅ Bottom sheet for base currency selection with search
//✅ Single StateFlow driving entire screen
//```
//
//---
//
//Commit message:
//```
//feat: add complete CurrencyListScreen with search, sort, and base currency selection