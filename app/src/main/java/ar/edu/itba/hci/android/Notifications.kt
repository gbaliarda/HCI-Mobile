package ar.edu.itba.hci.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.databinding.FragmentNotificationsBinding
import ar.edu.itba.hci.android.ui.profile.ProfileFragmentDirections

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
            // Get the possibles actions to translate through with this fragment
            val action = NotificationsDirections.actionNotificationsToNavigationProfile()
            findNavController().navigate(action)
        }
    }

}