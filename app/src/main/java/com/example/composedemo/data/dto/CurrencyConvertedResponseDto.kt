package com.example.composedemo.data.dto

import com.google.gson.annotations.SerializedName

data class CurrencyConvertedResponseDto(
    @SerializedName("data")
    val rates: Map<String, Double>
)
