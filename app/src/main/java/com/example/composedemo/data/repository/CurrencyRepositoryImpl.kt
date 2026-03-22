package com.example.composedemo.data.repository

import com.example.composedemo.data.remote.CurrencyApiService
import com.example.composedemo.domain.CurrencyRepository
import com.example.composedemo.domain.mapper.CurrencyMapper
import com.example.composedemo.domain.model.Currency
import com.example.composedemo.utils.DefaultDispatcherProvider
import com.example.composedemo.utils.UiState
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrencyApiService,
    private val dispatcherProvider: DefaultDispatcherProvider
) : CurrencyRepository {
    override suspend fun fetchAllCurrencyValues(
        baseCurrency: String,
        currencies: List<String>?
    ): UiState<List<Currency>> {
        return withContext(dispatcherProvider.io) {
            try {
                val currenciesString =
                    currencies?.takeIf { it.isNotEmpty() }?.joinToString(separator = ",")
                val currencyList = CurrencyMapper.mapCurrencyDtoToCurrencyList(
                    apiService.getAllCurrencyValues(baseCurrency, currenciesString)
                )
                UiState.Success(currencyList)
            } catch (e: HttpException) {
                UiState.Error("Some internet issue occurred {${e.message}}")
            } catch (e: IOException) {
                UiState.Error("Internet access problem")
            }
        }
    }
}