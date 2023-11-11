package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pricingapp.databinding.NewQuoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: NewQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Calculate quote button action
        val button = binding.calculateQuote
        button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val call = getRetrofit().create(XmlPlaceholderApi::class.java).getQuote("xml")

                    runOnUiThread {
                        if (call.isSuccessful) {
                            val quote = call.body()
                            Log.d("QuoteActivity", "API Response: $quote")
                        } else {
                            Log.e("QuoteActivity", "API Error Response: ${call.errorBody()?.string()}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("QuoteActivity", "Exception during API call: ${e.message}", e)
                }
            }
        }

        // Menu button
        val returnHome = binding.returnHome
        returnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Retrofit library for API responses
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://testt.free.beeceptor.com/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }
}
