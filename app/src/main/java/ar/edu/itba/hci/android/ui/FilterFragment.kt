package ar.edu.itba.hci.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.Communicator
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.FragmentFilterBinding
import ar.edu.itba.hci.android.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterFragment : BottomSheetDialogFragment(), RadioGroup.OnCheckedChangeListener {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        communicator = activity as Communicator

        binding.radioGroup.setOnCheckedChangeListener(this)

        binding.switchFavorite.setOnClickListener {
            Toast.makeText(context, "Switch", Toast.LENGTH_SHORT).show()
            communicator.passData("Switch")
        }

        return binding.root
    }

    override fun onCheckedChanged(p0: RadioGroup?, idRadio: Int) {
        when(idRadio) {
            binding.radioCateg.id -> Toast.makeText(context, "Ordenar categoría", Toast.LENGTH_SHORT).show()
            binding.radioDate.id -> Toast.makeText(context, "Ordenar fecha", Toast.LENGTH_SHORT).show()
            binding.radioDiff.id -> Toast.makeText(context, "Ordenar dificultad", Toast.LENGTH_SHORT).show()
            binding.radioScore.id -> Toast.makeText(context, "Ordenar puntuación", Toast.LENGTH_SHORT).show()
        }
    }

}