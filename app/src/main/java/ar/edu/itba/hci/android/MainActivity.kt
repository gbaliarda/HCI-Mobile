package ar.edu.itba.hci.android
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.AttributeSet
import android.widget.Toast
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ar.edu.itba.hci.android.databinding.ActivityMainBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionFragmentDirections
import ar.edu.itba.hci.android.ui.execution.ExecutionService
import ar.edu.itba.hci.android.ui.home.HomeFragmentDirections
import ar.edu.itba.hci.android.ui.routine.Routine
import ar.edu.itba.hci.android.ui.routine.RoutineFragment
import java.lang.NumberFormatException
import ar.edu.itba.hci.android.ui.home.HomeFragment
import ar.edu.itba.hci.android.ui.execution.ExecutionViewModel
import ar.edu.itba.hci.android.ui.execution.ExecutionViewModelFactory
import ar.edu.itba.hci.android.ui.routine.Exercise

class MainActivity : AppCompatActivity() {

    private val app: MainApplication
        get() = application as MainApplication

    private lateinit var binding: ActivityMainBinding
    private val mainViewmodel: MainViewModel by viewModels()
    private val exViewModel: ExecutionViewModel by viewModels { ExecutionViewModelFactory(app) }


    private var service:ExecutionService? = null
    private val serviceIntent:Intent by lazy {
        Intent(this, ExecutionService::class.java).also {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as ExecutionService.Binder
            this@MainActivity.service = binder.service
            binder.service.model = exViewModel
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            this@MainActivity.service = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = insets.left,
                right = insets.right,
                top = insets.top
            )
            binding.navView.updatePadding(bottom = insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val miniPlayer = binding.miniPlayer
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        val uri: Uri? = intent.data
        if (uri != null) {
            val path: String = uri.toString()
            val pathArgs = path.split("?")[1].split("=")
            var getId: Int? = -1
            if (pathArgs[0] == "id")
                try {
                    getId = pathArgs[1].toInt()
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToNavigationRoutine(getId)
                    navController.navigate(action)
                } catch (ex: Exception) {
                    Toast.makeText(this, "Error loading routine", Toast.LENGTH_LONG).show()
                }
        }

        miniPlayer.setOnClickListener {
            navController.navigate(R.id.executionFragment)
        }

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (mainViewmodel.isExercising) {
                if (destination.label != "fragment_execution") {
                    binding.miniPlayer.visibility = View.VISIBLE
                } else
                    binding.miniPlayer.visibility = View.GONE
            } else {
                binding.miniPlayer.visibility = View.GONE
            }
        }

        binding.controller.play.setOnClickListener { pauseOrPlay() }

        binding.controller.cancelButton.setOnClickListener {
            binding.miniPlayer.visibility = View.GONE
            mainViewmodel.isExercising = false

            val intent = Intent("PAUSE")
            sendBroadcast(intent)

            exViewModel.routineID = -1
        }

        binding.controller.nextExercise.setOnClickListener {
            exViewModel.nextExercise()
        }

        binding.controller.previousExercise.setOnClickListener {
            exViewModel.prevExercise()
        }

        exViewModel.finished.observe(this, {
            if (!it) return@observe
            binding.miniPlayer.visibility = View.GONE
            mainViewmodel.isExercising = false
            Toast.makeText(this, "Rutina completada", Toast.LENGTH_SHORT).show()
            exViewModel.routineID = -1
        })

        exViewModel.isFirstExercise.observe(this, {
            if (it) binding.controller.previousExercise.visibility = View.INVISIBLE
            else binding.controller.previousExercise.visibility = View.VISIBLE
        })

        exViewModel.currentExercise.observe(this, {
            binding.controller.exerciseName.text = it.exercise.name
            binding.controller.exerciseCount.text = getString(
                R.string.execution_exercise_count,
                exViewModel.currentExerciseIndex/2 + 1,
                exViewModel.executionList.size/2
            )
            if (it.exercise.repetitionType == Exercise.RepetitionType.TIMES) {
                binding.controller.play.alpha = 0.2f
                binding.controller.play.isEnabled = false
                binding.controller.bottomText.text = getString(R.string.miniplayer_repetitions, it.exercise.repetitionValue)
            } else {
                binding.controller.play.alpha = 1.0f
                binding.controller.play.isEnabled = true

                val seconds:Int =
                    if(it.exercise.repetitionType == Exercise.RepetitionType.MINUTES)
                        it.exercise.repetitionValue * 60
                    else it.exercise.repetitionValue

                exViewModel.timer.value = seconds
            }

            exViewModel.isPlaying.value = false
        })

        exViewModel.timer.observe(this, {
            binding.controller.bottomText.text = getString(R.string.miniplayer_timer, it)
        })


        exViewModel.timerRunning.observe(this, {
            binding.controller.play.setImageDrawable(
                when (it) {
                    true -> {
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, null)
                    }
                    false -> {
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, null)
                    }
                }
            )
        })

        exViewModel.isPlaying.observe(this, {
            if(it) startTimer()
            else pauseTimer()
        })
    }

    // Update action bar with the nav controller
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    override fun onStop() {
        super.onStop()
        unbindService()
        stopService(serviceIntent)
    }

    private fun pauseOrPlay() {
        exViewModel.timerRunning.value?.let {
            exViewModel.isPlaying.value = !it
        }
    }

    private fun startTimer() {
        applicationContext.startForegroundService(serviceIntent)
        service?.resume()
    }

    private fun pauseTimer() {
        service?.pause()
    }

    private fun bindService() {
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        unbindService(serviceConnection)
        service = null
    }
}