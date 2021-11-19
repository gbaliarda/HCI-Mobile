package ar.edu.itba.hci.android.ui.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.MainViewModel
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.FragmentRoutineBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionViewModel
import ar.edu.itba.hci.android.ui.execution.ExecutionViewModelFactory
import com.google.android.material.snackbar.Snackbar

class RoutineFragment : Fragment(), RatingBar.OnRatingBarChangeListener {
    private val args: RoutineFragmentArgs by navArgs()

    private var _binding: FragmentRoutineBinding? = null
    private val binding get() = _binding!!

    private val app: MainApplication by lazy { requireActivity().application as MainApplication }
    private val model: RoutineViewModel by viewModels {
        RoutineViewModelFactory(args.routineId, app)
    }

    private lateinit var exerciseAdapter: ExerciseAdapter
    private val mainViewModel : MainViewModel by activityViewModels()
    private val exViewModel : ExecutionViewModel by activityViewModels {ExecutionViewModelFactory(app)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseAdapter = ExerciseAdapter(requireContext())

        binding.exerciseRecycler.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecycler.adapter = exerciseAdapter

        binding.shareButton.setOnClickListener { shareHandler() }
        binding.ratingBar.onRatingBarChangeListener = this
        binding.ratingBar.stepSize = 1F
        binding.startButton.setOnClickListener { startHandler() }
        binding.likeButton.setOnClickListener {
            if (model.liked.value != null) {
                model.liked.value = !model.liked.value!!
            }
        }

        model.routine.observe(viewLifecycleOwner, {
            exerciseAdapter.cycles = it.cycles
            binding.routineName.text = it.name
            binding.description.text = it.description
            binding.time.text = getString(R.string.routine_minutes, it.durationMinutes)
            binding.difficulty.text = it.difficulty
            binding.spinner.visibility = View.GONE
            binding.content.visibility = View.VISIBLE

            when(model.routine.value?.id == exViewModel.routineID) {
                true -> binding.startButton.text = getString(R.string.routine_start_button_continue)
                false -> binding.startButton.text = getString(R.string.routine_start_button)
            }
        })

        model.liked.observe(viewLifecycleOwner, {
            likeHandler(it)
        })
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun likeHandler(liked: Boolean) {
        binding.likeButton.setImageDrawable(
            when (liked) {
                true -> ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite, null)
                false -> ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite_border, null)
            }
        )
    }

    private fun shareHandler() {
        model.routine.value?.let {
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/x-uri")
                .setChooserTitle(getString(R.string.share_title))
                .setText(it.shareLink)
                .startChooser()
        }
    }

    private fun reviewHandler() {
        notImplemented()
    }

    private fun startHandler() {
        mainViewModel.isExercising = true
        if(exViewModel.routineID != model.routine.value?.id) {
            exViewModel.reset()

            val action = RoutineFragmentDirections.actionNavigationRoutineToExecution1Fragment()
            model.routine.value?.let {
                action.routineID = it.id
            }
            findNavController().navigate(action)
        }
        else findNavController().navigate(R.id.executionFragment)
    }

    private fun notImplemented() {
        Snackbar.make(binding.root, "Not Implemented", Snackbar.LENGTH_SHORT)
            .show()
    }
}
