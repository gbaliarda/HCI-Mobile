package ar.edu.itba.hci.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ar.edu.itba.hci.android.databinding.ActivityMainBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionFragmentDirections
import ar.edu.itba.hci.android.ui.home.HomeFragmentDirections
import ar.edu.itba.hci.android.ui.routine.Routine
import ar.edu.itba.hci.android.ui.routine.RoutineFragment
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        val uri:Uri? = intent.data
        if (uri != null) {
            val path:String = uri.toString()
            val pathArgs = path.split("?")[1].split("=")
            var getId:Int? = -1
            if (pathArgs[0] == "id")
                try {
                    getId = pathArgs[1].toInt()
                    val action = HomeFragmentDirections.actionNavigationHomeToNavigationRoutine().setRoutineId(getId)
                    navController.navigate(action)
                } catch(ex:Exception) {
                    Toast.makeText(this, "Error loading routine", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Update action bar with the nav controller
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

}