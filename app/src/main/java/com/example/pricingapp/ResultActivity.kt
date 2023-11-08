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
