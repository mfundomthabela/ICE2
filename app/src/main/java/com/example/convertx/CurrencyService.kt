package com.example.convertx

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("/v1/convert")
    fun convertCurrency(
        @Query("api_key") apiKey: String,
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String,
        @Query("amount") amount: Double
    ): Call<CurrencyResponse>
}
