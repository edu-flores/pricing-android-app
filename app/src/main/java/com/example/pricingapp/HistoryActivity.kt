package com.example.pricingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.QuoteHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: QuoteHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
