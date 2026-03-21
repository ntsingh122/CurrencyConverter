package com.example.composedemo.domain

import com.example.composedemo.domain.model.Currency
import com.example.composedemo.utils.UiState

interface CurrencyRepository {
    suspend fun fetchAllCurrencyValues(baseCurrency: String): UiState<List<Currency>>
}