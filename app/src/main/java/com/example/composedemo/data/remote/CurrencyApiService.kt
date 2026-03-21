package com.example.composedemo.data.remote

import com.example.composedemo.data.dto.CurrencyConvertedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("v1/latest")
    suspend fun getAllCurrencyValues(
        @Query("base_currency") baseCurrency: String
    ): CurrencyConvertedResponseDto
}