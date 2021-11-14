package ar.edu.itba.hci.android.ui.execution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.FragmentExecution1Binding
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.ui.routine.RoutineFragmentDirections
import ar.edu.itba.hci.android.ui.routine.RoutineViewModel

class Execution1Fragment : Fragment() {
    private var _binding:FragmentExecution1Binding? = null
    private val binding get() = _binding!!

    private val model: Execution1ViewModel by viewModels() // CODIGO VIEWMODEL


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExecution1Binding.inflate(inflater, container, false)

        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButtom.setOnClickListener{
            // Get the possibles actions to translate through with this fragment
            val action = Execution1FragmentDirections.actionExecution1FragmentToNavigationRoutine()
            // Navigate to execution 1 fragment
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}