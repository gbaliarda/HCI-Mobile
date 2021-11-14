package ar.edu.itba.hci.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.api.model.Routine
import ar.edu.itba.hci.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val app:MainApplication by lazy {
        requireActivity().application as MainApplication
    }

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory(app) }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter:RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RoutineAdapter(requireContext())

        //TODO: Recyclerview horizontal

        binding.recycler?.layoutManager = LinearLayoutManager(context)
        binding.recycler?.adapter = adapter

        homeViewModel.routines.observe(viewLifecycleOwner, {
            adapter.routines = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}