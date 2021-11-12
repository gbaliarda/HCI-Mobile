package ar.edu.itba.hci.android.ui.execution

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.itba.hci.android.R

class Execution1Fragment : Fragment() {

    companion object {
        fun newInstance() = Execution1Fragment()
    }

    private lateinit var a1ViewModel: Execution1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_execution1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        a1ViewModel = ViewModelProvider(this).get(Execution1ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}