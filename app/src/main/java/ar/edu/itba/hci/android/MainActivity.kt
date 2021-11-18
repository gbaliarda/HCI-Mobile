package ar.edu.itba.hci.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ar.edu.itba.hci.android.databinding.ActivityMainBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionFragmentDirections
import ar.edu.itba.hci.android.ui.home.HomeFragmentDirections
import ar.edu.itba.hci.android.ui.routine.Routine
import ar.edu.itba.hci.android.ui.routine.RoutineFragment
import java.lang.NumberFormatException
import ar.edu.itba.hci.android.ui.home.HomeFragment
import ar.edu.itba.hci.android.ui.execution.ExecutionViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewmodel : MainViewModel by viewModels()
    private val exViewModel : ExecutionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val miniPlayer = binding.miniPlayer
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        if (AppPreferences(this).authToken == null) {
            navController.navigate(R.id.login2)
        }

        val uri:Uri? = intent.data
        if (uri != null) {
            val path:String = uri.toString()
            val pathArgs = path.split("?")[1].split("=")
            var getId:Int? = -1
            if (pathArgs[0] == "id")
                try {
                    getId = pathArgs[1].toInt()
                    val action = HomeFragmentDirections.actionNavigationHomeToNavigationRoutine(getId)
                    navController.navigate(action)
                } catch(ex:Exception) {
                    Toast.makeText(this, "Error loading routine", Toast.LENGTH_LONG).show()
                }
        }

        miniPlayer.setOnClickListener {
            navController.navigate(R.id.executionFragment)
        }

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if  (mainViewmodel.isExercising) {
                if (destination.label != "fragment_execution") {
                    binding.miniPlayer.visibility = View.VISIBLE
                }
                else
                    binding.miniPlayer.visibility = View.GONE
            }
            else {
                binding.miniPlayer.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences(this).authToken == null) {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.login2)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.controller.pause.setOnClickListener { pauseOrPlay() }
        binding.controller.play.setOnClickListener { pauseOrPlay() }


        binding.controller.cancelButton.setOnClickListener {
            binding.miniPlayer.visibility = View.GONE
            mainViewmodel.isExercising = false
        }
    }

    // Update action bar with the nav controller
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    private fun pauseOrPlay() {
        if (!binding.controller.play.isVisible) {
            binding.controller.pause.visibility = View.GONE
            binding.controller.play.visibility = View.VISIBLE
        }
        else {
            binding.controller.pause.visibility = View.VISIBLE
            binding.controller.play.visibility = View.GONE
        }

    }
}