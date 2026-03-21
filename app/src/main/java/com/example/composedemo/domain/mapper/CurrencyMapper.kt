package com.example.composedemo.domain.mapper

import com.example.composedemo.data.dto.CurrencyConvertedResponseDto
import com.example.composedemo.domain.model.Currency
import com.example.composedemo.domain.model.CurrencyInfo

object CurrencyMapper {
    fun mapCurrencyDtoToCurrencyList(currencyConvertedResponseDto: CurrencyConvertedResponseDto): List<Currency> {
        return currencyConvertedResponseDto.rates
            .map { (code, rate) ->
                Currency(
                    code = code,
                    rate = rate,
                    name = CurrencyInfo.getName(code),
                    flag = CurrencyInfo.getFlag(code)
                )
            }
            .sortedBy { it.code }
    }
}