package ar.edu.itba.hci.android.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Routine
import ar.edu.itba.hci.android.databinding.RoutineCardBinding

class RoutineAdapter(private val context:Context)
    : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    var routines:PagedList<Routine>? = null
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
        val item = routines!!.content[position]

        binding.name.text = item.name
        binding.description.text = item.difficulty
        binding.startRoutineButton.setOnClickListener { start(item) }
        binding.share.setOnClickListener { share(item) }
        binding.favorite.setOnClickListener { favorite(item) }
    }

    fun start(routine:Routine) {

    }

    fun share(routine:Routine) {

    }

    fun favorite(routine: Routine) {

    }

    override fun getItemCount() = if(routines == null) 0 else routines!!.content.size

    class ViewHolder(val binding:RoutineCardBinding)
        : RecyclerView.ViewHolder(binding.root) {
    }
}