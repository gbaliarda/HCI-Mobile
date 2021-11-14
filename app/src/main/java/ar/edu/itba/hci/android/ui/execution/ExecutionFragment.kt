package ar.edu.itba.hci.android.ui.execution

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ar.edu.itba.hci.android.databinding.FragmentExecutionBinding
import androidx.navigation.fragment.findNavController

class ExecutionFragment : Fragment() {
    private var _binding:FragmentExecutionBinding? = null
    private val binding get() = _binding!!

    private val model: ExecutionViewModel by viewModels() // CODIGO VIEWMODEL


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentExecutionBinding.inflate(inflater, container, false)

        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButtom.setOnClickListener{
            // Get the possibles actions to translate through with this fragment
            val action = ExecutionFragmentDirections.actionExecutionFragmentToNavigationRoutine()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}