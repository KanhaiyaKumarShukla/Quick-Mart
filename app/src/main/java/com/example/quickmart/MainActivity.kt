package com.example.quickmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.quickmart.databinding.ActivityMainBinding
import com.example.quickmart.fragments.AccountFragment
import com.example.quickmart.fragments.CategoriesFragment
import com.example.quickmart.fragments.HomeFragment
import com.example.quickmart.fragments.MyOrderFragment
import com.example.quickmart.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        navController=navHostFragment.navController
        binding.btmNaviView.apply {
            setupWithNavController(navController)
        }
//        NavigationUI.setupWithNavController(
//            binding.btmNaviView, navController
//        )
        binding.btmNaviView.setOnNavigationItemSelectedListener{

            when(it.itemId){
                R.id.nav_home -> {
                    if(navController.currentDestination?.id==R.id.homeFragment) return@setOnNavigationItemSelectedListener true
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_order->{
                    if(navController.currentDestination?.id==R.id.myOrderFragment) return@setOnNavigationItemSelectedListener true
                    navController.navigate(R.id.myOrderFragment)
                    true
                }
                R.id.nav_categories->{
                    if(navController.currentDestination?.id==R.id.categoriesFragment) return@setOnNavigationItemSelectedListener true
                    navController.navigate(R.id.categoriesFragment)
                    true
                }
                R.id.nav_account->{
                    if(navController.currentDestination?.id==R.id.accountFragment) return@setOnNavigationItemSelectedListener true
                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment -> {
                    binding.btmNaviView.selectedItemId = R.id.nav_home
                    binding.btmNaviView.visibility=View.VISIBLE
                }
                R.id.accountFragment -> {
                    binding.btmNaviView.selectedItemId = R.id.nav_account
                    binding.btmNaviView.visibility=View.VISIBLE
                }
                R.id.myOrderFragment -> {
                    binding.btmNaviView.selectedItemId = R.id.nav_order
                    binding.btmNaviView.visibility=View.VISIBLE
                }
                R.id.categoriesFragment -> {
                    binding.btmNaviView.selectedItemId = R.id.nav_categories
                    binding.btmNaviView.visibility=View.VISIBLE
                }

                else -> {
                    binding.btmNaviView.visibility = View.INVISIBLE
                }
            }
        }

    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.homeFragment) {
            navController.popBackStack(R.id.homeFragment, false)
        } else {
            super.onBackPressed()
        }
        /*
        * in popBackStack(), we have 2 parameter :
        * destinationId - The topmost destination to retain. represents the destination (screen) that already exists in our stack, and it will enable popBackStack to close
                           all screens that were opened after our destination screen
        * inclusion - a boolean that tells the popBackStack function if it should close the destination screen or not.
        * example - a user opens Profile screen from Home screen, and he presses Log out button. That action should close Profile and Home screens and the Login screen should be opened.
        * inclusive = false — our old Login screen will remain and it will be opened after closing Profile and Home.
          inclusive = true — our old Login screen will be closed together with Profile and Home; this will also require the user to manually open a new Login screen after closing all three. */


    }


}