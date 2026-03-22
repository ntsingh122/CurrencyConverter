package com.example.composedemo.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composedemo.domain.model.SortOrder
import com.example.composedemo.ui.BaseCurrencyBottomSheet
import com.example.composedemo.ui.list.component.CurrencyListItem
import com.example.composedemo.utils.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListScreen(
    viewModel: CurrencyListViewModel
) {
    val currencyListState by viewModel.currencyListState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    // ── Show snackbar on error ─────────────────────────────
    LaunchedEffect(currencyListState) {
        if (currencyListState is UiState.Error) {
            snackbarHostState.showSnackbar(
                message = (currencyListState as UiState.Error).message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // ── Scroll to top when sort changes ───────────────────
    LaunchedEffect(sortOrder) {
        listState.animateScrollToItem(0)
    }

    // ── Show FAB only when scrolled down ──────────────────
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // ── Bottom sheet ───────────────────────────────────────
    if (isBottomSheetVisible) {
        BaseCurrencyBottomSheet(
            currentBase = baseCurrency,
            onCurrencySelected = { code ->
                viewModel.onBaseCurrencyChange(code)
                viewModel.onBottomSheetVisibilityChange(false)
            },
            onDismiss = {
                viewModel.onBottomSheetVisibilityChange(false)
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        // ── TopAppBar ──────────────────────────────────────
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Currency Rates",
                        style = typography.titleLarge
                    )
                },
                actions = {
                    // base currency chip
                    AssistChip(
                        onClick = {
                            viewModel.onBottomSheetVisibilityChange(true)
                        },
                        label = {
                            Text(
                                text = baseCurrency,
                                style = typography.labelLarge
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Change base currency"
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface,
                    titleContentColor = colors.onSurface
                )
            )
        },

        // ── FAB ────────────────────────────────────────────
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = colors.primaryContainer,
                    contentColor = colors.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ── Search bar ─────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        text = "Search currencies...",
                        style = typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                )
            )

            // ── Sort chips ─────────────────────────────────
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(SortOrder.entries) { sort ->
                    FilterChip(
                        selected = sortOrder == sort,
                        onClick = { viewModel.onSortOrderChange(sort) },
                        label = {
                            Text(
                                text = when (sort) {
                                    SortOrder.CODE_ASC  -> "A → Z"
                                    SortOrder.CODE_DESC -> "Z → A"
                                    SortOrder.RATE_ASC  -> "Rate ↑"
                                    SortOrder.RATE_DESC -> "Rate ↓"
                                },
                                style = typography.labelMedium
                            )
                        },
                        leadingIcon = if (sortOrder == sort) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null
                    )
                }
            }

            // ── Content area ───────────────────────────────
            when (val state = currencyListState) {

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = colors.primary
                            )
                            Text(
                                text = "Fetching rates...",
                                style = typography.bodyMedium,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = colors.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = state.message,
                                style = typography.bodyMedium,
                                color = colors.error,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { viewModel.fetchRates() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primary
                                )
                            ) {
                                Text(
                                    text = "Retry",
                                    style = typography.labelLarge
                                )
                            }
                        }
                    }
                }

                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        // empty search result
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = colors.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "No currencies found",
                                    style = typography.titleMedium,
                                    color = colors.onSurfaceVariant
                                )
                                Text(
                                    text = "Try a different search term",
                                    style = typography.bodyMedium,
                                    color = colors.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 4.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.data,
                                key = { it.code }
                            ) { currency ->
                                CurrencyListItem(currency = currency, onClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}



/*
*
┌─────────────────────────────────────┐
│  Currency Rates      [🇺🇸 USD ▼]     │  ← TopAppBar
│                      base currency  │
├─────────────────────────────────────┤
│  🔍 Search currencies...            │  ← Search bar
├─────────────────────────────────────┤
│                                     │
│  ┌─────────────────────────────┐    │
│  │  🇦🇪  AED          3.6730  │      │  ← CurrencyListItem card
│  │       UAE Dirham            │    │
│  └─────────────────────────────┘    │
│                                     │
│  ┌─────────────────────────────┐    │
│  │  🇦🇺  AUD          1.5320  │      │
│  │       Australian Dollar     │    │
│  └─────────────────────────────┘    │
│                                     │
│  ┌─────────────────────────────┐    │
│  │  🇬🇧  GBP          0.7920  │      │
│  └─────────────────────────────┘    │
│            ...                      │
├─────────────────────────────────────┤

        ↓ tap [🇺🇸 USD ▼]

┌─────────────────────────────────────┐
│  ▬▬▬                                │  ← ModalBottomSheet
│  Select base currency               │
│  ─────────────────────────────────  │
│  🔍 Search...                       │
│  ─────────────────────────────────  │
│  🇦🇪  AED   UAE Dirham             │
│  🇦🇺  AUD   Australian Dollar      │
│  🇬🇧  GBP   British Pound          │
│  🇮🇳  INR   Indian Rupee           │
│  🇺🇸  USD   US Dollar              │
│             ...                     │
└─────────────────────────────────────┘
*
* */