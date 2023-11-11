package com.example.pricingapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface XmlPlaceholderApi {
    @GET("https://www.abfs.com/xml/aquotexml.asp")
    suspend fun getQuote(@QueryMap params: Map<String, String>): Response<QuoteResponse>
}
