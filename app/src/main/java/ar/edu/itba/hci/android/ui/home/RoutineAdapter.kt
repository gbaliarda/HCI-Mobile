package ar.edu.itba.hci.android.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Routine
import ar.edu.itba.hci.android.databinding.RoutineCardBinding
import java.util.*

class RoutineAdapter(private val fragment:HomeFragment)
    : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    var routines:List<Routine> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RoutineCardBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = routines[position]

        binding.name.text = item.name

        if (Locale.getDefault().displayLanguage == "español")
            when(item.difficulty) {
                "rookie", "beginner" -> binding.description.text = "Principiante"
                "intermediate" -> binding.description.text = "Intermedia"
                "advanced", "expert" -> binding.description.text = "Avanzada"
            }
        else
            binding.description.text = item.difficulty[0].uppercase() + item.difficulty.substring(1)

        binding.startRoutineButton.setOnClickListener { start(item) }
        binding.root.setOnClickListener{ details(item) }
    }

    private fun details(routine:Routine) {
        val dir = HomeFragmentDirections.actionNavigationHomeToNavigationRoutine(routine.id)
        fragment.findNavController().navigate(dir)
    }

    private fun start(routine:Routine) {
        val directions = HomeFragmentDirections.actionNavigationHomeToExecutionFragment()
        directions.routineID = routine.id
        fragment.mainModel.isExercising = true

        if(fragment.exModel.routineID != routine.id) {
            fragment.exModel.reset()
        }

        fragment.findNavController().navigate(directions)

    }

    override fun getItemCount() = routines.size

    class ViewHolder(val binding:RoutineCardBinding)
        : RecyclerView.ViewHolder(binding.root) {
    }
}