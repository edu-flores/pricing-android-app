package com.example.pricingapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface XmlPlaceholderApi {
    @GET
    suspend fun getQuote(@Url url: String): Response<QuoteResponse>
}
