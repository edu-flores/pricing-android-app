package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation
        setupMenuButtons()
    }

    // Create a new quote or view past quotes
    private fun setupMenuButtons() {
        val quoteButton = binding.menuQuoteButton
        val historyButton = binding.menuHistoryButton

        quoteButton.setOnClickListener {
            navigateToQuoteActivity()
        }

        historyButton.setOnClickListener {
            navigateToHistoryActivity()
        }
    }

    // New quote
    private fun navigateToQuoteActivity() {
        val intent = Intent(this, QuoteActivity::class.java)
        startActivity(intent)
    }

    // Past quotes
    private fun navigateToHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}
