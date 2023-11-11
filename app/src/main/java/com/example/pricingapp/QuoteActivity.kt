package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.pricingapp.databinding.NewQuoteBinding
import kotlinx.coroutines.GlobalScope
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

        // Database
        val db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "QuoteDatabase.db").build()

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

                            // Save in SQLite
                            GlobalScope.launch(Dispatchers.IO) {
                                val quote1 = Quote(fromPlace = "Boston, MA", toPlace = "Las Vegas, NV", price = 8000, transit = "90 days")
                                db.quoteDao().insert(quote1)
                            }

                            // Show result activity
                            val intent = Intent(this@QuoteActivity, ResultActivity::class.java)
                            val bundle = Bundle()
                            bundle.putString("from", "Boston, Massachusetts")
                            bundle.putString("to", "Las Vegas, Nevada")
                            bundle.putInt("price", 8000)
                            bundle.putString("transit", "90 days")
                            intent.putExtras(bundle)
                            startActivity(intent)
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
