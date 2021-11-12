package ar.edu.itba.hci.android.ui.execution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.itba.hci.android.R

class Execution2Fragment : Fragment() {

    companion object {
        fun newInstance() = Execution2Fragment()
    }

    private lateinit var viewModel: Execution2ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_execution2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Execution2ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}