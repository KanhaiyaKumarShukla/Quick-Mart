package com.example.quickmart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quickmart.R
import com.example.quickmart.adapters.CategoryAdapter
import com.example.quickmart.adapters.CategoryWiseProductAdapter
import com.example.quickmart.adapters.FilteredAdapter
import com.example.quickmart.adapters.SuggestionsAdapter
import com.example.quickmart.data.model.productsItem
import com.example.quickmart.databinding.FragmentHomeBinding
import com.example.quickmart.utils.AppConstant
import com.example.quickmart.utils.NetworkResult
import com.example.quickmart.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: MainViewModel.Factory

    private lateinit var mViewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var allData: List<productsItem>
    private lateinit var categoryWiseData:Map<String, List<productsItem>>
    private lateinit var allCategories:List<String>
    private lateinit var suggestionsAdapter: SuggestionsAdapter
    private lateinit var categoryWiseProductAdapter: CategoryWiseProductAdapter
    private var filteredProducts: Map<String, List<productsItem>> = emptyMap()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var moreProductsAdapter: FilteredAdapter
    private var categoryWiseLimitedProduct:MutableList<Pair<String, List<productsItem>>> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        if(!AppConstant.isInternetAvailable(requireContext())){
            navigateToError()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cartIv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myOrderFragment)
        }

        Glide.with(this)
            .load("https://i.pinimg.com/originals/62/d9/ee/62d9eee8d016b85b6ad3ad2da1ab7e01.png")
            .placeholder(R.drawable.baseline_wb_sunny_24)
            .override(150, 150)
            .into(binding.greetingIv)

        mViewModel = ViewModelProvider(this,
            MainViewModel.providesFactory(mViewModelFactory, null, null)
        )[MainViewModel::class.java]




        setupSearchView()

        mViewModel.productsLiveData.observe(viewLifecycleOwner) { result ->
            allData = when (result) {
                is NetworkResult.Success -> {
                    Log.d("data retrieved:", result.data.toString())
                    result.data!!
                }
                is NetworkResult.Error -> {
                    Log.d("data Retrieved", "error found!")
                    emptyList()
                }
                is NetworkResult.Loading -> {
                    Log.d("data Retrieved:", "Loading...")
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.dataLayout.visibility = View.INVISIBLE
                    binding.shimmerLayout.startShimmer()
                    emptyList()
                }
                else -> {
                    emptyList()
                }
            }
        }
        mViewModel.categories.observe(viewLifecycleOwner) { result ->
            allCategories = when (result) {
                is NetworkResult.Success -> {
                    Log.d("data retrieved:", result.data.toString())
                    result.data!!
                }
                is NetworkResult.Error -> {
                    Log.d("data Retrieved", "error found!")
                    emptyList()
                }
                is NetworkResult.Loading -> {
                    Log.d("data Retrieved:", "Loading...")
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.dataLayout.visibility = View.INVISIBLE
                    binding.shimmerLayout.startShimmer()
                    emptyList()
                }
                else -> {
                    emptyList()
                }
            }
        }


        mViewModel.productsByCategoriesMap.observe(viewLifecycleOwner) { result ->
            categoryWiseData = when (result) {
                is NetworkResult.Success -> {
                    Log.d("data retrieved:", result.data.toString())
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.dataLayout.visibility = View.VISIBLE
                    result.data!!
                }
                is NetworkResult.Error -> {
                    Log.d("data Retrieved", "error found!")
                    navigateToError()
                    emptyMap()
                }
                is NetworkResult.Loading -> {
                    Log.d("data Retrieved:", "Loading...")
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.dataLayout.visibility = View.INVISIBLE
                    binding.shimmerLayout.startShimmer()
                    emptyMap()
                }
                else -> {
                    emptyMap()
                }
            }

            setupRecyclerView()
            setupCategoryView()
            setupImageSlider()
        }

    }

    private fun navigateToError() {
        val bundle:Bundle=Bundle().apply{
            putInt("destination", R.id.action_errorFragment_to_homeFragment)
        }
        findNavController().navigate(R.id.action_homeFragment_to_errorFragment, bundle)

    }

    private fun setupCategoryView() {
        // Create a LinearLayoutManager with horizontal orientation
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Set the LayoutManager and adapter for the RecyclerView
        binding.categoryRecycleView.layoutManager = layoutManager
        val category: List<Pair<String, String>> = listOf(
            "Category" to "https://cdn-icons-png.flaticon.com/512/6178/6178349.png",
            "Men's Clothing" to "https://th.bing.com/th/id/R.fa65310290d7d66b163862c43b6a794e?rik=PYXUaLUmwBuqig&riu=http%3a%2f%2fmenscraze.com%2fwp-content%2fuploads%2f2016%2f05%2fDavid-Gandy-Style.jpg&ehk=boua1Dz78BNmsgP1hebpkQYgugR2%2fzmRWN%2bGvDXAnk8%3d&risl=&pid=ImgRaw&r=0",
            "Women's Clothing" to "https://1.bp.blogspot.com/-3df860veUW4/VIlI3PT1JaI/AAAAAAAABBA/rgNpy8hoWFc/s1600/Fashion%2BFor%2BWomen%2B(3).jpg",
            "Electronics" to "https://th.bing.com/th/id/R.046c210c2fb97aa8471ea6640cf0ccca?rik=P5cJGCI13FdS%2fA&riu=http%3a%2f%2fdealsmaps.com%2fwp-content%2fuploads%2f2022%2f03%2fThe-Must-Have-15-Electronics-Products-to-Improve-Your-Life-Quality-1.jpg&ehk=2N26QxEhIgPdRq9dXyINnwwbaHEF%2fDxG3vkrxEEOJH0%3d&risl=&pid=ImgRaw&r=0",
            "Jewelery" to "https://th.bing.com/th/id/OIP.RWrVjT_wqmvOE4wMIpRByAHaHa?rs=1&pid=ImgDetMain",
            "Sports" to "https://th.bing.com/th/id/OIP.t2rQXCTN7EioZMJSL43brAHaE7?rs=1&pid=ImgDetMain",
            "Footwear" to "https://rukminim1.flixcart.com/image/1664/1664/shoe/m/z/u/camel-g-4092y14-woodland-40-original-imadwjtqnv3pxhd9.jpeg?q=90",
            "Musical Instruments" to "https://th.bing.com/th/id/R.17fc86fbf2c8cc2829e3ccc0c98fd153?rik=eH8o%2bNczJnhxyA&riu=http%3a%2f%2fwallpapercave.com%2fwp%2fW0wqDHh.jpg&ehk=PEjyBsubCJxmuy7uUtJsSQxmhKZ0hVftT8mmqPpir8c%3d&risl=&pid=ImgRaw&r=0"

        )

        categoryAdapter= CategoryAdapter(category, categoryWiseData)
        binding.categoryRecycleView.adapter=categoryAdapter
        // set up category wise limited product in list:
        allCategories.forEach {
            if(categoryWiseData[it].isNullOrEmpty()) return@forEach
            val categoryProductsPair: Pair<String, List<productsItem>> = Pair(it, categoryWiseData[it]!!.take(6))
            categoryWiseLimitedProduct.add(categoryProductsPair)

        }
    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel("https://i.pinimg.com/originals/37/25/23/372523635665a97932a7b365dfd3eaa1.jpg"))
        imageList.add(SlideModel("https://newspaperads.ads2publish.com/wp-content/uploads/2018/04/lifestyle-shopping-mall-flat-25-off-ad-bombay-times-07-04-2018.png"))
        imageList.add(SlideModel("https://mfour.com/wp-content/uploads/2020/06/Spotted-New-Social-Media-Shopping-Habits.jpg"))
        imageList.add(SlideModel("https://graphicsfamily.com/wp-content/uploads/edd/2021/07/Samsung-Galaxy-Social-Media-Banner-Design-1536x864.jpg"))
        imageList.add(SlideModel("https://i.pinimg.com/736x/fe/77/ff/fe77ffc1cf4f10459af11435cccdcc9c.jpg"))
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupRecyclerView() {
        suggestionsAdapter = SuggestionsAdapter(emptyList()) { suggestion ->
            binding.searchView.setQuery(suggestion, false)
            binding.suggestionsRecyclerView.visibility = View.GONE
            // Perform search based on selected suggestion
            val products = filteredProducts[suggestion]
            inflateProductItems(products)
        }
        binding.suggestionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.suggestionsRecyclerView.adapter = suggestionsAdapter

        categoryWiseProductAdapter= CategoryWiseProductAdapter(categoryWiseLimitedProduct, findNavController(), R.id.action_homeFragment_to_productDetailFragment)
        binding.productForYouRv.layoutManager=LinearLayoutManager(requireContext())
        binding.productForYouRv.adapter=categoryWiseProductAdapter

        categoryWiseProductAdapter.setOnItemClickListener(object : CategoryWiseProductAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, model: Pair<String, List<productsItem>>) {
                val bundle=Bundle().apply{
                    putString("extra Data category", model.first)
                    putParcelableArrayList("extraData", ArrayList(model.second))
                }
                findNavController().navigate(R.id.action_homeFragment_to_filteredProductsFragment, bundle)
            }
        })

        moreProductsAdapter= FilteredAdapter(allData.selectRandomElements(allData.size))

        binding.moreProductRv.layoutManager=GridLayoutManager(requireContext(), 2)
        binding.moreProductRv.adapter=moreProductsAdapter

        moreProductsAdapter.setOnClickListener(object : FilteredAdapter.OnClickListener{
            override fun onclick(position: Int, model: productsItem) {
                val bundle=Bundle().apply{
                      putParcelable("extraData", model)
                }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && filteredProducts.containsKey(query)) {
                    val products = filteredProducts[query]
                    binding.dataLayout.visibility=View.VISIBLE
                    binding.suggestionsRecyclerView.visibility = View.INVISIBLE
                    inflateProductItems(products)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    suggestionsAdapter.updateSuggestions(emptyList())
                    binding.dataLayout.visibility=View.VISIBLE
                    binding.suggestionsRecyclerView.visibility = View.INVISIBLE
                    return false
                }
                binding.dataLayout.visibility=View.INVISIBLE
                binding.suggestionsRecyclerView.visibility = View.VISIBLE
                filteredProducts = AppConstant.filterProducts(newText, allData)
                //Log.d("query: ", filteredProducts.toString())
                displaySuggestions(filteredProducts)
                return true
            }
        })
    }

    private fun displaySuggestions(filteredProducts: Map<String, List<productsItem>>) {
        val suggestions = filteredProducts.keys.toList()
        binding.suggestionsRecyclerView.visibility = if (suggestions.isEmpty()) View.GONE else View.VISIBLE
        suggestionsAdapter.updateSuggestions(suggestions)
    }

    private fun inflateProductItems(products: List<productsItem>?) {
        // Implement the logic to inflate product items based on the productIds

        binding.searchedItemsRecyclerView.visibility=if(products.isNullOrEmpty())View.INVISIBLE else View.VISIBLE
        if(!products.isNullOrEmpty() && products.size>1) {
            binding.dataLayout.visibility=View.INVISIBLE
            val adapter=FilteredAdapter(products)
            val layoutManager= GridLayoutManager(requireContext(), 2)

            // Set the LayoutManager and adapter for the RecyclerView
            binding.searchedItemsRecyclerView.layoutManager = layoutManager

            binding.searchedItemsRecyclerView.adapter = adapter

            adapter.setOnClickListener(object : FilteredAdapter.OnClickListener{
                override fun onclick(position: Int, model: productsItem) {
                    val bundle=Bundle().apply{
                        putParcelable("extraData", model)
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
                }
            })
        }else if(products?.size==1){
            val bundle=Bundle().apply{
                putParcelable("extraData", products[0])
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
        }

    }

    private fun <T> List<T>.selectRandomElements(numElements: Int): List<T> {
        val shuffledList = this.shuffled() // Shuffle the list
        return shuffledList.take(numElements) // Select the first numElements from the shuffled list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        binding.shimmerLayout.startShimmer()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }

}