package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.pricingapp.databinding.QuoteHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: QuoteHistoryBinding
    private val quoteList: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize recyclerView and adapter
        val recyclerView = binding.historyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter(quoteList)
        recyclerView.adapter = adapter

        // Database
        val db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "QuoteDatabase.db").build()

        // Add sample data to the database
        /*
        GlobalScope.launch(Dispatchers.IO) {
            val quote1 = Quote(fromPlace = "City A", toPlace = "City B", price = 100, transit = "3 days")
            db.quoteDao().insert(quote1)
        }
        */

        // Retrieve quotes from the database
        GlobalScope.launch(Dispatchers.IO) {
            val quotes = db.quoteDao().getAllQuotes()
            launch(Dispatchers.Main) {
                for (quote in quotes) {
                    quoteList.add(quote)
                }
                adapter.notifyDataSetChanged()
            }
        }

        // Menu buttons
        val homeButton = binding.returnHomeButton

        // Switch activity listeners
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
