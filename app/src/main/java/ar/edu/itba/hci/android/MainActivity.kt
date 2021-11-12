package ar.edu.itba.hci.android

import android.os.Bundle
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ar.edu.itba.hci.android.databinding.ActivityMainBinding
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragmentActivityMain) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            // Apply the insets as padding to the view. Here we're setting all of the
            // dimensions, but apply as appropriate to your layout. You could also
            // update the views margin if more appropriate.
            view.updatePadding(0, insets.top, 0, 0)

            // Return CONSUMED if we don't want the window insets to keep being passed
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}