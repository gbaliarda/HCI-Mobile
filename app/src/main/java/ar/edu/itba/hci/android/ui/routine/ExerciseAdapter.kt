package ar.edu.itba.hci.android.ui.routine

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.ExerciseCardLayoutBinding
import com.google.android.material.snackbar.Snackbar

class ExerciseAdapter(private val context:Context)
    : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    var exercises:List<Exercise> = listOf()
        @SuppressLint("NotifyDataSetChanged")
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
        val item = exercises[position]

        binding.name.text = item.name

        var repetitions = when(item.repetitionType) {
            Exercise.RepetitionType.TIMES -> context.getString(R.string.exercise_repetitions, item.repetitionValue)
            Exercise.RepetitionType.SECONDS -> context.getString(R.string.exercise_seconds, item.repetitionValue)
        }

        if(item.sets > 1)
            repetitions += context.getString(R.string.exercise_sets, item.sets)

        binding.repetitions.text = repetitions
    }

    override fun getItemCount() = exercises.size

    class ViewHolder(val binding:ExerciseCardLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.infoButton.setOnClickListener {
                    Snackbar.make(binding.root,"Not Implemented", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }
}