package ar.edu.itba.hci.android.ui.routine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.databinding.ExerciseCardLayoutBinding

class ExerciseAdapter()
    : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    var exercises:List<Exercise> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExerciseCardLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = exercises[position%exercises.size];

        binding.name.text = item.name;

        if(item.repetitions != null)
            binding.repetitions.text = "${item.repetitions} repeticiones"
        else if(item.seconds != null)
            binding.repetitions.text = "${item.seconds} segundos"

        if(item.sets != null)
            binding.repetitions.text = "${binding.repetitions.text} x${item.sets}";
    }

    override fun getItemCount() = Int.MAX_VALUE;

    class ViewHolder(val binding:ExerciseCardLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.infoButton.setOnClickListener {
                    println("Clicked ${binding.name.text} card")
                }
            }
    }
}