package ar.edu.itba.hci.android.ui.execution

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ar.edu.itba.hci.android.databinding.FragmentExecutionBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.itba.hci.android.MainActivity
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.channelID
import java.util.concurrent.TimeUnit

class ExecutionFragment : Fragment() {
    private var _binding:FragmentExecutionBinding? = null
    private val binding get() = _binding!!

    private val args:ExecutionFragmentArgs by navArgs()

    private val app:MainApplication by lazy {
        requireActivity().application as MainApplication
    }

    private val model: ExecutionViewModel by viewModels { ExecutionViewModelFactory(app) }
    private var service:ExecutionService? = null
    private val serviceIntent:Intent by lazy {
        Intent(requireContext(), ExecutionService::class.java).also {
            requireActivity().bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as ExecutionService.Binder
            this@ExecutionFragment.service = binder.service
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            this@ExecutionFragment.service = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExecutionBinding.inflate(inflater, container, false)
        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.routineID != -1) {
            model.routineID = args.routineID
        }

        binding.playOrPauseButton.setOnClickListener {
            if (!model.running.value!!) {
                startTimer()
            }
            else {
                pauseTimer()
            }
        }

        binding.backToRoutineButton.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            model.nextExercise()
        }

        binding.previousButton.setOnClickListener {
            model.prevExercise()
        }

        model.timer.observe(viewLifecycleOwner, {
            binding.timer.text = it.toString()
        })

        model.currentExercise.observe(viewLifecycleOwner, {
            binding.exerciseName.text = it.name
            binding.timer.text = it.repetitionValue.toString()
        })

        model.running.observe(viewLifecycleOwner, {
            if(it) binding.playOrPauseButton.text = getString(R.string.pausa)
            else binding.playOrPauseButton.text = getString(R.string.play)
        })
    }

    override fun onStart() {
        super.onStart()
        bindService()
    }

    override fun onStop() {
        super.onStop()
        unbindService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startTimer() {
        service!!.model = model
        requireActivity().applicationContext.startForegroundService(serviceIntent)
        service!!.resume()
    }

    private fun pauseTimer() {
        service!!.pause()
    }

    private fun bindService() {
        requireActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        requireActivity().unbindService(serviceConnection)
        service = null
    }
}