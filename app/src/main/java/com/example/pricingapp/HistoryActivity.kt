package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.QuoteHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: QuoteHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menu buttons
        val homeButton = binding.returnHomeButton

        // Switch activity listeners
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
