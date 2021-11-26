package ar.edu.itba.hci.android.ui.routine

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.CycleHeaderBinding
import ar.edu.itba.hci.android.databinding.ExerciseCardBinding
import com.google.android.material.snackbar.Snackbar

class ExerciseAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_HEADER = -1
        private const val ITEM_EXERCISE = -2
    }

    private var items: List<Any> = listOf()

    var cycles: List<Cycle> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value

            val newList = mutableListOf<Any>()
            for (c in cycles) {
                newList.add(c)
                for (e in c.exercises)
                    newList.add(e)
            }

            items = newList
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Cycle -> ITEM_HEADER
            is Exercise -> ITEM_EXERCISE
            else -> throw IllegalStateException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_EXERCISE -> {
                val binding = ExerciseCardBinding.inflate(LayoutInflater.from(parent.context))
                ExerciseViewHolder(context,binding)
            }
            ITEM_HEADER -> {
                val binding = CycleHeaderBinding.inflate(LayoutInflater.from(parent.context))
                HeaderViewHolder(context,binding)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ExerciseViewHolder -> holder.bind(items[position] as Exercise)
            is HeaderViewHolder -> holder.bind(items[position] as Cycle)
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount() = items.size

    class ExerciseViewHolder(
        private val context: Context,
        private val binding: ExerciseCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ex: Exercise) {
            binding.name.text = ex.name

            val repetitions = when (ex.repetitionType) {
                Exercise.RepetitionType.TIMES -> context.getString(
                    R.string.exercise_repetitions,
                    ex.repetitionValue
                )
                Exercise.RepetitionType.SECONDS -> context.getString(
                    R.string.exercise_seconds,
                    ex.repetitionValue
                )
                Exercise.RepetitionType.MINUTES -> context.getString(
                    R.string.exercise_minutes,
                    ex.repetitionValue
                )
            }

            binding.repetitions.text = repetitions

            binding.infoButton.setOnClickListener {
                val bottomSheetFragment = ExerciseFragment()
                val bundle = Bundle()
                bundle.putString("name", ex.name)
                bundle.putString("difficulty", ex.difficulty)
                bundle.putString("group", ex.group)
                bundle.putString("desc", ex.description)
                bottomSheetFragment.arguments = bundle

                bottomSheetFragment.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    "bottomSheetFragment"
                )
            }
        }
    }

    class HeaderViewHolder(
        private val context: Context,
        private val binding: CycleHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cycle:Cycle) {
            binding.name.text = cycle.name
            binding.repetitions.text = context.getString(R.string.cycle_header_repetitions, cycle.repetitions)
        }
    }
}