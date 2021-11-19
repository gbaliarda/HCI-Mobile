package ar.edu.itba.hci.android.ui.execution

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import ar.edu.itba.hci.android.databinding.FragmentExecutionBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.ui.routine.Exercise
import ar.edu.itba.hci.android.ui.routine.ExerciseFragment

class ExecutionFragment : Fragment() {
    private var _binding:FragmentExecutionBinding? = null
    private val binding get() = _binding!!

    private val args:ExecutionFragmentArgs by navArgs()

    private val app:MainApplication by lazy {
        requireActivity().application as MainApplication
    }

    private val model: ExecutionViewModel by activityViewModels() { ExecutionViewModelFactory(app) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExecutionBinding.inflate(inflater, container, false)
        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.routineID != -1) {
            model.routineID = args.routineID
        }

        binding.content.visibility = View.GONE
        binding.spinner.visibility = View.VISIBLE

        binding.playOrPauseButton.setOnClickListener {
            model.isPlaying.value = !model.timerRunning.value!!
        }

        binding.backToRoutineButton.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.refreshButton.setOnClickListener {
            model.currentExercise.value?.let {
                val ex = it.exercise
                val seconds = when(ex.repetitionType) {
                    Exercise.RepetitionType.MINUTES -> ex.repetitionValue * 60
                    Exercise.RepetitionType.SECONDS -> ex.repetitionValue
                    else -> return@let
                }
                model.isPlaying.value = false
                model.timer.value = seconds
            }
        }

        binding.exerciseInfo.setOnClickListener {
            model.currentExercise.value?.let {
                val ex = it.exercise
                val bottomSheetFragment = ExerciseFragment()
                val bundle = Bundle()
                bundle.putString("name", ex.name)
                bundle.putString("difficulty", ex.difficulty)
                bundle.putString("group", ex.group)
                bundle.putString("desc", ex.description)
                bottomSheetFragment.arguments = bundle

                bottomSheetFragment.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    "bottomSheetFragment"
                )
            }
        }

        binding.nextButton.setOnClickListener {
            model.nextExercise()
        }

        binding.previousButton.setOnClickListener {
            model.prevExercise()
        }

        model.timer.observe(viewLifecycleOwner, {
            binding.timer.text = "${it}s"
        })

        model.routine.observe(viewLifecycleOwner, {
            binding.routineName.text = it.name
        })

        model.currentExercise.observe(viewLifecycleOwner, {
            binding.exerciseName.text = it.exercise.name
            binding.cycleName.text = it.cycle.name
            binding.cycleSet.text = getString(R.string.execution_sets, it.cycleRepetition, it.cycle.repetitions)
            binding.exerciseCount.text = getString(R.string.execution_exercise_count, model.currentExerciseIndex+1, model.executionList.size)

            when(it.exercise.repetitionType) {
                Exercise.RepetitionType.TIMES -> {
                    binding.timer.text = "x${it.exercise.repetitionValue}"
                    binding.playOrPauseButton.isEnabled = false
                    binding.refreshButton.visibility = View.GONE
                }
                Exercise.RepetitionType.SECONDS -> {
                    binding.playOrPauseButton.isEnabled = true
                    model.timer.value = it.exercise.repetitionValue
                    binding.refreshButton.visibility = View.VISIBLE
                }
                Exercise.RepetitionType.MINUTES -> {
                    binding.playOrPauseButton.isEnabled = true
                    model.timer.value = it.exercise.repetitionValue * 60
                    binding.refreshButton.visibility = View.VISIBLE
                }
            }
        })

        model.loading.observe(viewLifecycleOwner, {
            when(it) {
                true -> {
                    binding.spinner.visibility = View.VISIBLE
                    binding.content.visibility = View.GONE
                }
                false -> {
                    binding.spinner.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                }
            }
        })

        model.timerRunning.observe(viewLifecycleOwner, {
            if(it) binding.playOrPauseButton.text = getString(R.string.pause)
            else binding.playOrPauseButton.text = getString(R.string.play)
        })

        model.isFirstExercise.observe(viewLifecycleOwner, {
            binding.previousButton.visibility = when(it) {
                true -> View.GONE
                else -> View.VISIBLE
            }
        })

        model.finished.observe(viewLifecycleOwner, {
            if(!it) return@observe

            findNavController().popBackStack()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}