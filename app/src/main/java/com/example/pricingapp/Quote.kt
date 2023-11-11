package com.example.pricingapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fromPlace: String,
    val toPlace: String,
    val price: Int,
    val transit: String
)
