package com.example.quickmart.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quickmart.R
import com.example.quickmart.data.model.products
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.utils.AppConstant
import java.util.Locale


class CategoryAdapter(private val productList: List<Pair<String, String>>, private val productsItems: Map<String, List<productsItem>>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_view_home, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val product = productList[position]

        holder.categoryName.text = product.first
        Glide.with(holder.itemView.context)
            .load(product.second)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            if(position==0){
                it.findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
                return@setOnClickListener
            }
            val products=productsItems[product.first.lowercase(Locale.ROOT)]
            Log.d("products outcome", products.toString())
            Log.d("category: ", product.first)
            val bundle = Bundle().apply {
                putString("extra Data category", product.first)
                putParcelableArray("extraData", products?.toTypedArray())
            }
            it.findNavController().navigate(R.id.action_homeFragment_to_filteredProductsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}