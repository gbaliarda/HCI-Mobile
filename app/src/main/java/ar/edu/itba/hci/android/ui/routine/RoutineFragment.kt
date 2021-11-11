package ar.edu.itba.hci.android.ui.routine

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.RoutineFragmentBinding

class RoutineFragment : Fragment() {

    companion object {
        fun newInstance() = RoutineFragment()
    }

    private val model: RoutineViewModel by viewModels()

    private val exerciseAdapter = ExerciseAdapter();

    private var _binding: RoutineFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RoutineFragmentBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.exercises.observe(viewLifecycleOwner, Observer {
            exerciseAdapter.exercises = it
        })

        binding.exerciseRecycler.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecycler.adapter = exerciseAdapter;
        binding.routineName.text = "Rutina1"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}