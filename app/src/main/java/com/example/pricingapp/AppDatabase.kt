package com.example.pricingapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao
}