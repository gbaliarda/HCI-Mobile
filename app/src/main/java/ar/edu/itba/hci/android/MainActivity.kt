package ar.edu.itba.hci.android

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ar.edu.itba.hci.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        binding.controller.pause.setOnClickListener { pauseOrPlay() }
        binding.controller.play.setOnClickListener { pauseOrPlay() }
    }



    // Update action bar with the nav controller
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    private fun pauseOrPlay() {
        if (!binding.controller.play.isVisible) {
            binding.controller.pause.visibility = View.INVISIBLE
            binding.controller.play.visibility = View.VISIBLE
        }
        else {
            binding.controller.pause.visibility = View.VISIBLE
            binding.controller.play.visibility = View.INVISIBLE
        }

    }

}