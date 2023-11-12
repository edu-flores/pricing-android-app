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

            bundle.putString("fromCity", item.fromCity)
            bundle.putString("fromState", item.fromState)
            bundle.putString("fromZip", item.fromZip)
            bundle.putString("toCity", item.toCity)
            bundle.putString("toState", item.toState)
            bundle.putString("toZip", item.toZip)
            bundle.putString("price", item.price)
            bundle.putString("transitTime", item.transitTime)
            bundle.putString("expirationDate", item.transitTime)

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
            binding.itemFromTextView.text = "From: ${quote.fromCity}, ${quote.fromState}"
            binding.itemToTextView.text = "To: ${quote.toCity}, ${quote.toState}"
            binding.itemPriceTextView.text = "$${quote.price} USD"
            binding.itemTransitTextView.text = "TT: ${quote.transitTime}"
        }
    }
}
