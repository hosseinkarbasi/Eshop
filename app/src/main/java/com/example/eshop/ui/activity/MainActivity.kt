package com.example.eshop.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.eshop.R
import com.example.eshop.databinding.ActivityMainBinding
import com.example.eshop.ui.fragments.cart.CartViewPagerAdapter
import com.example.eshop.utils.conntectivitymanager.MyState
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNetworkConnection()
        setupNavController()
        getPreferences()
        CartViewPagerAdapter(supportFragmentManager, lifecycle)


    }

    private fun checkNetworkConnection() {
        viewModel.state.collectWithRepeatOnLifecycle(this) {
            when (it) {
                MyState.Error -> {
                    binding.navFragment.gone()
                    binding.bottomNavigationView.gone()
                    binding.connectivityLayout.visible()
                }
                MyState.Fetched -> {
                    binding.navFragment.visible()
                    binding.bottomNavigationView.visible()
                    binding.connectivityLayout.gone()
                }
            }
        }
    }

    private fun setupNavController() = binding.apply {
        val navController by lazy {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_fragment) as NavHostFragment
            navHostFragment.navController
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productFragment -> bottomNavigationView.gone()
                R.id.productsListFragment -> bottomNavigationView.gone()
                R.id.searchFragment -> bottomNavigationView.gone()
                R.id.reviewsFragment -> bottomNavigationView.gone()
                else -> bottomNavigationView.visible()
            }
        }
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
        }
    }

    private fun getPreferences() {
        viewModel.preferences.collectWithRepeatOnLifecycle(this) {
            val info = viewModel.preferences.first()
            val mode = info.theme.mode
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            if (currentMode != mode) {
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

}