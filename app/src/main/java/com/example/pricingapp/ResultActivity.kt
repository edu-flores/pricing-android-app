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

        // Get bundle to display result
        val bundle = intent.extras
        val from = bundle?.getString("from")
        val to = bundle?.getString("to")
        val price = bundle?.getInt("price")
        val transit = bundle?.getString("transit")

        // Display content
        binding.fromTextView.text = "From: ${from.orEmpty()}"
        binding.toTextView.text = "To: ${to.orEmpty()}"
        binding.priceTextView.text = "$${price} USD"
        binding.transitTextView.text = "Estimated Transit:\n ${transit.orEmpty()}"

        // Menu buttons
        val quotesButton = binding.savedQuotesButton
        val homeButton = binding.returnHomeButton

        // Switch activity listeners
        quotesButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
