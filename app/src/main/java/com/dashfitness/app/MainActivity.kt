package com.dashfitness.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var runDataDAO: RunDatabaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            setupNavigation()
        }
    }

    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout);

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar);

        setupActionBarWithNavController(navController, binding.drawerLayout);

        binding.navigationView.setupWithNavController(navController);

//        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
//            val toolbar = supportActionBar ?: return@addOnDestinationChangedListener;
//            when (destination.id) {
//                R.id.home -> {
////                    toolbar.title = "Home"
////                    toolbar.setDisplayShowTitleEnabled(false)
//                }
//                else -> {
////                    toolbar.setDisplayShowTitleEnabled(true)
//                }
//            }
//        }
    }
}
