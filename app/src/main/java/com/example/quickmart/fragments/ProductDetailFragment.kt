package com.example.quickmart.fragments

import android.os.Bundle
import android.text.Spanned
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.quickmart.R
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.databinding.FragmentHomeBinding
import com.example.quickmart.databinding.FragmentProductDetailBinding
import io.github.glailton.expandabletextview.EXPAND_TYPE_POPUP
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product: productsItem? = arguments?.getParcelable("extraData")

        product?.let {
            Glide.with(this)
                .load(product.image)
                .override(450, 450)
                .into(binding.productIv)

            binding.productNameTv.text=product.title
            binding.currentPrice.text=formatPriceWithIndianCurrency(product.price)
            val offset=offSetValue(product.price* 74.5)
            val price = String.format("%.1f", offset.first)
            val spannableString = SpannableString(price).apply {
                setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            binding.pastPrice.text = spannableString
            binding.offTv.text=offset.second.toString() + "%"

            binding.ratingBar.rating=product.rating.rate.toFloat()
            binding.ratingNoTv.text="("+product.rating.count.toString()+")"
            binding.expandProductDetailTv
                .setAnimationDuration(500)
                .setReadMoreText("View More")
                .setReadLessText("View Less")
                .setCollapsedLines(6)
                .setIsExpanded(true)
                .setIsUnderlined(true)
                .setExpandType(EXPAND_TYPE_POPUP)
                .setEllipsizedTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
            binding.expandProductDetailTv.text=modifiedDetails(product.title, product.description, product.category)
            binding.detailedRatingTv.text=product.rating.rate.toString()
            binding.detailedRatingCount.text=product.rating.count.toString()

            binding.excellentProgressBar.progress=50
            binding.excellentCountTV.text=ratingCount(50, product.rating.count)

            binding.veryGoodProgressBar.progress=20
            binding.veryGoodCountTV.text=ratingCount(20, product.rating.count)

            binding.goodProgressBar.progress=15
            binding.goodCountTV.text=ratingCount(15, product.rating.count)

            binding.averageProgressBar.progress=10
            binding.averageCountTV.text=ratingCount(10, product.rating.count)
            binding.poorProgressBar.progress=5
            binding.poorCountTV.text=ratingCount(5, product.rating.count)

        }
    }

    private fun ratingCount(percent: Int, rate: Int): String {
        val result=percent*rate/100
        return result.toString()
    }

    private fun modifiedDetails(title: String, description: String, category:String): String {
        val lines = description.split(".") // Split the input string into lines based on full stop
        val result = StringBuilder()

        result.append("\u25AA Name: $title \n$category.\n")
        // Add bullet points to each line
        lines.forEachIndexed { _, line ->
            val formattedLine = if (line.isNotBlank()) "\u25AA $line\n" else "" // Add bullet point and line break
            result.append(formattedLine)
        }
        return result.toString()

    }

    private fun offSetValue(price: Double): Pair<Double, Int> {
        val offsetPercentage = Random.nextInt(20, 51)
        val pastPrice= (price*100)/ (100-offsetPercentage).toDouble()
        return Pair(pastPrice,offsetPercentage)
    }
    private fun formatPriceWithIndianCurrency(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        return format.format(price*74.5)
    }
}