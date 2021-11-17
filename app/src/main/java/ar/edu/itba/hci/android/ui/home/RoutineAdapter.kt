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

class RoutineAdapter(private val fragment:Fragment)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = routines[position]

        binding.name.text = item.name
        binding.description.text = item.difficulty
        binding.startRoutineButton.setOnClickListener { start(item) }
        binding.share.setOnClickListener { share(item) }
        binding.favorite.setOnClickListener { favorite(item) }
        binding.root.setOnClickListener{ details(item) }
    }

    fun details(routine:Routine) {
        val dir = HomeFragmentDirections.actionNavigationHomeToNavigationRoutine(routine.id)
        fragment.findNavController().navigate(dir)
    }

    fun start(routine:Routine) {

    }

    fun share(routine:Routine) {

    }

    fun favorite(routine: Routine) {

    }

    override fun getItemCount() = routines.size

    class ViewHolder(val binding:RoutineCardBinding)
        : RecyclerView.ViewHolder(binding.root) {
    }
}