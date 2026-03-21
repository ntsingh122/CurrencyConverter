package com.example.composedemo.data.repository

import com.example.composedemo.data.remote.CurrencyApiService
import com.example.composedemo.domain.CurrencyRepository
import com.example.composedemo.domain.mapper.CurrencyMapper
import com.example.composedemo.domain.model.Currency
import com.example.composedemo.utils.UiState
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrencyApiService
) : CurrencyRepository {
    override suspend fun fetchAllCurrencyValues(baseCurrency: String): UiState<List<Currency>> {
        return try {
            val currencyList = CurrencyMapper.mapCurrencyDtoToCurrencyList(
                apiService.getAllCurrencyValues(baseCurrency)
            )
            UiState.Success(currencyList)
        } catch (e: HttpException) {
            UiState.Error("Some internet issue occurred {${e.message}}")
        } catch (e: IOException) {
            UiState.Error("Internet access problem")
        }
    }
}