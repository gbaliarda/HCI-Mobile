package ar.edu.itba.hci.android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.Notifications
import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.databinding.FragmentProfileBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionFragmentDirections

class ProfileFragment : Fragment() {

    private lateinit var notificationsViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsButton = binding.settings

        settingsButton.setOnClickListener {
            // Get the possibles actions to translate through with this fragment
            val action = ProfileFragmentDirections.actionNavigationProfileToNotifications()
            // Navigate to execution 1 fragment
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}