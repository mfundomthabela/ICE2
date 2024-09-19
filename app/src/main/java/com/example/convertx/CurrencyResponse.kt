package com.example.convertx

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("rates") val rates: Map<String, ConversionData>
)

data class ConversionData(
    @SerializedName("rate") val rate: Double
)
