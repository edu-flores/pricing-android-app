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
        val from = bundle?.getString("from").orEmpty()
        val to = bundle?.getString("to").orEmpty()
        val price = bundle?.getInt("price") ?: 0
        val transit = bundle?.getString("transit").orEmpty()

        binding.fromTextView.text = "From: $from"
        binding.toTextView.text = "To: $to"
        binding.priceTextView.text = "$${price} USD"
        binding.transitTextView.text = "Estimated Transit:\n $transit"
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
