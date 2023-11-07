package com.example.pricingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pricingapp.databinding.NewQuoteBinding

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: NewQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
