package com.example.composedemo.domain

import com.example.composedemo.domain.model.Currency
import com.example.composedemo.utils.UiState

interface CurrencyRepository {
    suspend fun fetchAllCurrencyValues(baseCurrency: String = "USD", currencies: List<String>? = null): UiState<List<Currency>>
}