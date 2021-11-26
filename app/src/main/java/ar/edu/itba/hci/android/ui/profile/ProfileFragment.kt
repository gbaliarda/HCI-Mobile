package ar.edu.itba.hci.android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.*
import ar.edu.itba.hci.android.NotificationsFragment
import ar.edu.itba.hci.android.databinding.FragmentProfileBinding
import ar.edu.itba.hci.android.ui.execution.ExecutionFragmentDirections
import ar.edu.itba.hci.android.ui.login.LoginViewModel
import ar.edu.itba.hci.android.ui.login.LoginViewModelFactory

class ProfileFragment : Fragment() {

    private val app: MainApplication by lazy {
        requireActivity().application as MainApplication
    }
    private val profileViewModel: ProfileViewModel by activityViewModels { ProfileViewModelFactory(app) }
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsButton = binding.settings
        val userFirstName = binding.userProfileName
        val userEmail = binding.userProfileName2
        val logoutBtn = binding.cerrarSesion
        val userImg = binding.profileImg

        settingsButton.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavigationProfileToNotifications()
            findNavController().navigate(action)
        }

        //Get colorPrimary
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data

        binding.refresh?.setColorSchemeColors(colorPrimary)
        binding.refresh?.isRefreshing = true
        binding.refresh?.setOnRefreshListener {
            profileViewModel.refreshProfile()
        }

        logoutBtn.setOnClickListener {
            profileViewModel.logout()
        }

        profileViewModel.logoutResult.observe(viewLifecycleOwner,
            Observer { logoutResult ->
                if (logoutResult == null || !logoutResult.isLoggedOut)
                    return@Observer
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
            })

        profileViewModel.profileResult.observe(viewLifecycleOwner,{
            if(it.name != null) {
                userFirstName.text = it.name
                userEmail.text = it.email
                when(it.avatarUrl?.substring(0,2)) {
                    "F1" -> userImg.setImageResource(R.drawable.ic_profile_f1)
                    "F2" -> userImg.setImageResource(R.drawable.ic_profile_f2)
                    "F3" -> userImg.setImageResource(R.drawable.ic_profile_f3)
                    "F4" -> userImg.setImageResource(R.drawable.ic_profile_f4)
                    "F5" -> userImg.setImageResource(R.drawable.ic_profile_f5)
                    "M1" -> userImg.setImageResource(R.drawable.ic_profile_m1)
                    "M2" -> userImg.setImageResource(R.drawable.ic_profile_m2)
                    "M3" -> userImg.setImageResource(R.drawable.ic_profile_m3)
                    else -> userImg.setImageResource(R.drawable.ic_profile_default)
                }
            }
            binding.refresh?.isRefreshing = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}