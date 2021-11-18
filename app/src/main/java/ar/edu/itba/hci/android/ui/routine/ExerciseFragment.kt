package ar.edu.itba.hci.android.ui.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.itba.hci.android.databinding.FragmentExerciseBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExerciseFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        binding.exName.text = arguments?.getString("name")
        binding.exDesc.text = arguments?.getString("desc")
        binding.exGrp.text = arguments?.getString("group")
        binding.exDiff.text = arguments?.getString("difficulty")

        return binding.root
    }
}