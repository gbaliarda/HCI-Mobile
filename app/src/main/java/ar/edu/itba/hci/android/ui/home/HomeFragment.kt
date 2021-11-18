package ar.edu.itba.hci.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.api.model.Routine
import ar.edu.itba.hci.android.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private val app:MainApplication by lazy {
        requireActivity().application as MainApplication
    }

    private val model: HomeViewModel by activityViewModels { HomeViewModelFactory(app) }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter:RoutineAdapter

    private var routines:List<Routine> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val bottomSheetFragment = FilterFragment()

        binding.filterBtn.setOnClickListener {
            bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
        }

        model.onlyFavorite.observe(viewLifecycleOwner, {
            sortAndFilter(null)
        })

        model.ordering.observe(viewLifecycleOwner, {
            sortAndFilter(null)
        })

        return binding.root
    }

    private fun sortAndFilter(search:String?) {
        val fav = model.onlyFavorite.value
        val order = model.ordering.value
        adapter.routines = routines.filter { fav == null || !fav || it.metadata.favorite == fav }
            .sortedWith(
                when(order) {
                    Ordering.DATE -> compareBy { it.date }
                    Ordering.DIFFICULTY -> compareBy { it.difficulty }
                    Ordering.SCORE -> compareBy { it.metadata.score }
                    else -> compareBy { it.date }
                }
            )
        search?.let { text ->
            adapter.routines = adapter.routines.filter { text.lowercase() in it.name.lowercase() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RoutineAdapter(this)

        //TODO: Recyclerview horizontal

        binding.recycler?.layoutManager = LinearLayoutManager(context)
        binding.recycler?.adapter = adapter

        model.routines.observe(viewLifecycleOwner, {
            adapter.routines = it.content
            routines = it.content
        })

        (binding.search as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                sortAndFilter(newText)
                return true
            }
        })
            
        model.routines.observe(viewLifecycleOwner, {
            adapter.routines = it.content
            binding.spinner?.visibility = View.GONE
            binding.content?.visibility = View.VISIBLE
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}