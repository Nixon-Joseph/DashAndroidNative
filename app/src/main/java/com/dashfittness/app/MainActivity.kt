package com.dashfittness.app

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dashfittness.app.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        if (savedInstanceState == null) {
            setupNavigation();
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
