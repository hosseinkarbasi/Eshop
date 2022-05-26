package com.example.eshop.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.eshop.R
import com.example.eshop.databinding.ActivityMainBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
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

        setupNavController()
        getPreferences()

    }

    private fun setupNavController() {
        val navController by lazy {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_fragment) as NavHostFragment
            navHostFragment.navController
        }
        binding.bottomNavigationView.setupWithNavController(navController)
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