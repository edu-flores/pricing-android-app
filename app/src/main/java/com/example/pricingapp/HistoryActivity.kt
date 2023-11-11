package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.pricingapp.databinding.QuoteHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: QuoteHistoryBinding
    private val quoteList: MutableList<Quote> = mutableListOf()
    private val db = Room.databaseBuilder(applicationContext,
        AppDatabase::class.java, "QuoteDatabase.db").build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuoteHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup recycler view and database
        initializeRecyclerView()
        setupDatabase()

        // Navigation
        setupButtons()
    }

    // Recycler view and adapter
    private fun initializeRecyclerView() {
        val recyclerView = binding.historyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter(quoteList, this)
        recyclerView.adapter = adapter
    }

    // SQLite
    private fun setupDatabase() {
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

    // Show confirmation dialog to delete all quotes
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete")
        builder.setMessage("Do you really want to delete all quotes?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            GlobalScope.launch(Dispatchers.IO) {
                db.quoteDao().deleteAllQuotes()
            }
            quoteList.clear()
            binding.historyRecyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Return to menu
    private fun setupButtons() {
        binding.returnHomeButton.setOnClickListener {
            navigateToMainActivity()
        }

        binding.deleteQuotesButton.setOnClickListener {
            showDialog()
        }
    }

    // Menu
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
