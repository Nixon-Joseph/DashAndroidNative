package com.dashfittness.app

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dashfittness.app.databinding.ActivityMainBinding

const val PERMISSION_REQUEST: Int = 42;

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            setupNavigation();

            if (
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    android.Manifest.permission.INTERNET
                ), PERMISSION_REQUEST);
            }
        }
    }

    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout);

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment);

        setSupportActionBar(binding.toolbar);

        setupActionBarWithNavController(navController, binding.drawerLayout);

        binding.navigationView.setupWithNavController(navController);

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolbar = supportActionBar ?: return@addOnDestinationChangedListener;
            when (destination.id) {
                R.id.home -> {
                    toolbar.setDisplayShowTitleEnabled(false)
                }
                else -> {
                    toolbar.setDisplayShowTitleEnabled(true)
                }
            }
        }
    }
}
