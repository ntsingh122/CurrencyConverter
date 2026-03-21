package com.example.composedemo.domain.mapper

import com.example.composedemo.data.dto.CurrencyConvertedResponseDto
import com.example.composedemo.domain.model.Currency

object CurrencyMapper {
    fun mapCurrencyDtoToCurrencyList(currencyConvertedResponseDto: CurrencyConvertedResponseDto): List<Currency> {
        return currencyConvertedResponseDto.rates.map { (code, value) ->
            Currency(code, value.toString())
        }
            .sortedBy { it.code }
    }
}