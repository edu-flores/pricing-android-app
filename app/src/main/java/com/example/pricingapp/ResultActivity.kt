package com.example.pricingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.QuoteResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: QuoteResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
