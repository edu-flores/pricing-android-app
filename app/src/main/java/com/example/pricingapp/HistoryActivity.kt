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

        // Setup recycler view and database
        initializeRecyclerView()
        setupDatabase()

        // Navigation
        setupMenuButtons()
    }

    // Recycler view and adapter
    private fun initializeRecyclerView() {
        val recyclerView = binding.historyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter(quoteList)
        recyclerView.adapter = adapter
    }

    // SQLite
    private fun setupDatabase() {
        val db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "QuoteDatabase.db").build()

        GlobalScope.launch(Dispatchers.IO) {
            val quotes = db.quoteDao().getAllQuotes()
            launch(Dispatchers.Main) {
                for (quote in quotes) {
                    quoteList.add(quote)
                }
                binding.historyRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    // Return to menu
    private fun setupMenuButtons() {
        binding.returnHomeButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    // Menu
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
