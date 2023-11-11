package com.example.pricingapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuoteDao {
    @Insert
    fun insert(quote: Quote): Long

    @Query("SELECT * FROM quotes ORDER BY id DESC")
    fun getAllQuotes(): List<Quote>

    @Update
    fun updateQuote(quote: Quote): Int

    @Delete
    fun deleteQuote(quote: Quote): Int
}