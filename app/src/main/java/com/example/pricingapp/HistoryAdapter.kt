package com.example.pricingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pricingapp.databinding.QuoteItemBinding

class HistoryAdapter(private val quoteList: List<Quote>, private val context: Context) :
    RecyclerView.Adapter<HistoryAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QuoteItemBinding.inflate(inflater, parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val item = quoteList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("from", item.fromPlace)
            bundle.putString("to", item.toPlace)
            bundle.putInt("price", item.price)
            bundle.putString("transit", item.transit)

            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

    inner class QuoteViewHolder(private val binding: QuoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: Quote) {
            binding.itemFromTextView.text = "From: ${quote.fromPlace}"
            binding.itemToTextView.text = "To: ${quote.toPlace}"
            binding.itemPriceTextView.text = "Price: $${quote.price} USD"
            binding.itemTransitTextView.text = "TT: ${quote.transit}"
        }
    }
}
