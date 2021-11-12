package ar.edu.itba.hci.android

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import ar.edu.itba.hci.android.databinding.FragmentNotificationsBinding
import ar.edu.itba.hci.android.databinding.FragmentProfileBinding
import ar.edu.itba.hci.android.ui.login.LoginViewModel
import ar.edu.itba.hci.android.ui.login.LoginViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [Notifications.newInstance] factory method to
 * create an instance of this fragment.
 */
class Notifications : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.back

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}