package com.example.quickmart.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quickmart.R
import com.example.quickmart.data.model.productsItem
import java.text.NumberFormat
import java.util.Locale

class FilteredAdapter (
    private val productList: List<productsItem>
) : RecyclerView.Adapter<FilteredAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val priceTextView: TextView = itemView.findViewById(R.id.price_tv)
        val ratingTextView: TextView = itemView.findViewById(R.id.rating_tv)
        val ratingCountTextView: TextView = itemView.findViewById(R.id.rating_count)
        val itemImageView:ImageView=itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_view, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        Log.d("filtered adapter", product.toString())
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.itemImageView)
        holder.titleTextView.text=product.title
        holder.priceTextView.text = formatPriceWithIndianCurrency(product.price)
        holder.ratingTextView.text = product.rating.rate.toString()
        holder.ratingCountTextView.text=formatRatingCount(product.rating.count)
    }

    private fun formatRatingCount(count: Int): String {
        return "($count)"
    }

    private fun formatPriceWithIndianCurrency(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return format.format(price)
    }
}