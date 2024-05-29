package com.example.quickmart.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmart.R
import com.example.quickmart.data.model.productsItem
import java.text.NumberFormat
import java.util.Locale

class CategoryWiseProductAdapter (
    private val productsCategories:List<Pair<String, List<productsItem>>>,
    private val navController: NavController,
    private val navigateToNextFragment:Int,
    private val rows:Int=1
):RecyclerView.Adapter<CategoryWiseProductAdapter.CategoryWiseViewHolder> (){

    private var itemClickListener: OnItemClickListener? = null

    inner class CategoryWiseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val categoryName=itemView.findViewById<TextView>(R.id.category_name)
        val moreTV=itemView.findViewById<TextView>(R.id.more_tv)
        val recyclerView=itemView.findViewById<RecyclerView>(R.id.product_recycler_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWiseViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.category_wise_products, parent, false)
        return CategoryWiseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsCategories.size
    }


    override fun onBindViewHolder(holder: CategoryWiseViewHolder, position: Int) {
        val product=productsCategories[position]
        holder.categoryName.text=modifiedCategoryName(product.first)
        val innerLayoutManager=GridLayoutManager(holder.recyclerView.context, rows, GridLayoutManager.HORIZONTAL, false)
        val productAdapter=FilteredAdapter(product.second)
        holder.recyclerView.layoutManager=innerLayoutManager
        holder.recyclerView.adapter=productAdapter
        productAdapter.setOnClickListener(object : FilteredAdapter.OnClickListener{
            override fun onclick(position: Int, model: productsItem) {
                val bundle= Bundle().apply{
                    putParcelable("extraData", model)
                }
                navController.navigate(navigateToNextFragment, bundle)
            }
        })
        if(rows==2)holder.moreTV.visibility = View.GONE
        else {
            holder.moreTV.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(position, product)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, model:Pair<String, List<productsItem>>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }


    private fun modifiedCategoryName(name:String):String{
        // Split the input string into words
        val words = name.split(" ")

        // Capitalize the first letter of each word
        val capitalizedWords = words.map { word ->
            word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        } }

        // Join the capitalized words back together
        return capitalizedWords.joinToString(" ")
    }

}