package ar.edu.itba.hci.android.ui.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.FragmentRoutineBinding
import com.google.android.material.snackbar.Snackbar

class RoutineFragment : Fragment() {
    private var _binding: FragmentRoutineBinding? = null
    private val binding get() = _binding!!

    private val model: RoutineViewModel by viewModels()
    private lateinit var exerciseAdapter:ExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRoutineBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseAdapter = ExerciseAdapter(requireContext())

        binding.exerciseRecycler.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecycler.adapter = exerciseAdapter

        binding.shareButton.setOnClickListener { shareHandler() }
        binding.reviewButton.setOnClickListener { reviewHandler() }
        binding.startButton.setOnClickListener { startHandler() }
        binding.likeButton.setOnClickListener {
            if(model.liked.value != null) {
                model.liked.value = !model.liked.value!!
            }
        }

        model.routine.observe(viewLifecycleOwner, {
            exerciseAdapter.exercises = it.exercises
            binding.routineName.text = it.name
            binding.time.text = getString(R.string.routine_minutes, it.durationMinutes)
            binding.exerciseCount.text = getString(R.string.routine_exercise_count, it.exercises.size)
        })
        model.liked.observe(viewLifecycleOwner, {
            likeHandler(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun likeHandler(liked:Boolean) {
        binding.likeButton.setImageDrawable(
            when(liked) {
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

        // Get the possibles actions to translate through with this fragment
        val action = RoutineFragmentDirections.actionNavigationRoutineToExecution1Fragment()
        findNavController().navigate(action)
    }

    private fun notImplemented() {
        Snackbar.make(binding.root,"Not Implemented", Snackbar.LENGTH_SHORT)
            .show()
    }
}
