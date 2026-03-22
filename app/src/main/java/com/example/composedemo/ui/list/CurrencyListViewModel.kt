package com.example.composedemo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composedemo.domain.CurrencyRepository
import com.example.composedemo.domain.model.Currency
import com.example.composedemo.domain.model.SortOrder
import com.example.composedemo.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    // ── Private state ──────────────────────────────────────
    private val _uiState = MutableStateFlow<UiState<List<Currency>>>(UiState.Loading)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.CODE_ASC)
    val sortOrder = _sortOrder.asStateFlow()

    private val _baseCurrency = MutableStateFlow("USD")
    val baseCurrency = _baseCurrency.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    @OptIn(FlowPreview::class)
    private val debouncedQuery = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()

    // ── Single public state for screen ─────────────────────
    val currencyListState: StateFlow<UiState<List<Currency>>> = combine(
        _uiState,
        debouncedQuery,
        _sortOrder
    ) { state, query, sort ->
        when (state) {
            is UiState.Loading -> UiState.Loading
            is UiState.Error   -> UiState.Error(state.message)
            is UiState.Success -> {
                val filtered = if (query.isBlank()) {
                    state.data
                } else {
                    state.data.filter { currency ->
                        currency.code.contains(query, ignoreCase = true) ||
                                currency.name.contains(query, ignoreCase = true)
                    }
                }
                UiState.Success(
                    when (sort) {
                        SortOrder.CODE_ASC  -> filtered.sortedBy { it.code }
                        SortOrder.CODE_DESC -> filtered.sortedByDescending { it.code }
                        SortOrder.RATE_ASC  -> filtered.sortedBy { it.rate }
                        SortOrder.RATE_DESC -> filtered.sortedByDescending { it.rate }
                    }
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    // ── Init ───────────────────────────────────────────────
    init {
        fetchRates()
    }

    // ── Public functions ───────────────────────────────────
    fun fetchRates() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = repository.fetchAllCurrencyValues(_baseCurrency.value)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSortOrderChange(sort: SortOrder) {
        _sortOrder.value = sort
    }

    fun onBaseCurrencyChange(code: String) {
        _baseCurrency.value = code
        fetchRates()
    }

    fun onBottomSheetVisibilityChange(visible: Boolean) {
        _isBottomSheetVisible.value = visible
    }
}