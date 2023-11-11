package com.example.pricingapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val fromCity: String,
    val fromState: String,
    val fromZip: String,
    val toCity: String,
    val toState: String,
    val toZip: String,
    val price: String,
    val transitTime: String,
    val expirationDate: String
)
