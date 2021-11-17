package ar.edu.itba.hci.android.ui.execution

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ar.edu.itba.hci.android.databinding.FragmentExecutionBinding
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.R
import java.util.concurrent.TimeUnit

class ExecutionFragment : Fragment() {
    private var _binding:FragmentExecutionBinding? = null
    private val binding get() = _binding!!

    var default = 30*1000L
    var timer = default
    var isStarted = false

    private val ExecutionViewModel: ExecutionViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExecutionBinding.inflate(inflater, container, false)
        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playOrPauseButton.setOnClickListener {
            if (!isStarted) {
                startTimer()
            }
            else {
                pauseTimer()
            }

        }

        binding.backToRoutineButton.setOnClickListener{
            findNavController().popBackStack()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countDownTimer.cancel()
    }

    //                                                        30 segundos ahora
    private var countDownTimer = object : CountDownTimer(timer, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d("timer", "onTick: ${millisUntilFinished/1000f}")
            //update text
            binding.timer.text = getString(R.string.formatted_time,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
        }

        override fun onFinish() {
            Toast.makeText(context,"end timer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer() {
        isStarted = true
        binding.playOrPauseButton.text = "${R.string.pausa}"
        countDownTimer.start()
    }

    private fun pauseTimer() {
        isStarted = false
        binding.playOrPauseButton.text = "${R.string.play}"
        countDownTimer.cancel()
    }

    private fun restTimer() {
        countDownTimer.cancel()
        timer = default
        startTimer()
    }


}