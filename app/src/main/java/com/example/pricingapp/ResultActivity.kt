package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.QuoteResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: QuoteResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Data
        displayResultContent()

        // Navigation
        setupMenuButtons()
    }

    // Show price and estimated transit
    private fun displayResultContent() {
        val bundle = intent.extras
        val fromCity = bundle?.getString("fromCity").orEmpty()
        val fromState = bundle?.getString("fromState").orEmpty()
        val fromZip = bundle?.getString("fromZip").orEmpty()
        val toCity = bundle?.getString("toCity").orEmpty()
        val toState = bundle?.getString("toState").orEmpty()
        val toZip = bundle?.getString("toZip").orEmpty()
        val price = bundle?.getString("price") ?: 0
        val transitTime = bundle?.getString("transitTime").orEmpty()
        val expirationDate = bundle?.getString("expirationDate").orEmpty()

        binding.fromTextView.text = "From: $fromCity, $fromState, $fromZip"
        binding.toTextView.text = "To: $toCity, $toState, $toZip"
        binding.priceTextView.text = "$${price} USD"
        binding.transitTextView.text = "Estimated Transit:\n $transitTime"
    }

    // Go to menu or view quote history
    private fun setupMenuButtons() {
        val quotesButton = binding.savedQuotesButton
        val homeButton = binding.returnHomeButton

        quotesButton.setOnClickListener {
            navigateToHistoryActivity()
        }

        homeButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    // Quote history
    private fun navigateToHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    // Menu
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
