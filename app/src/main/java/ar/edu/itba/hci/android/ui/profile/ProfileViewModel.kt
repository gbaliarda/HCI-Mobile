package ar.edu.itba.hci.android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.hci.android.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val application: MainApplication
    ) : ViewModel() {

    private val userRepository = application.userRepository

    val profileResult: LiveData<ProfileResult> by lazy {
        MutableLiveData<ProfileResult>().also {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val currUser = userRepository.getCurrentUser()
                    it.postValue(ProfileResult(name = currUser.firstName, email = currUser.email, avatarUrl = currUser.avatarUrl))
                } catch(ex:Exception) {
                    it.postValue(ProfileResult(name = null, email = null))
                }
            }
        }
    }

    private val _logoutResult = MutableLiveData<LogoutResult>()
    val logoutResult: LiveData<LogoutResult> = _logoutResult

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.logout()
                application.preferences.authToken = null
                _logoutResult.postValue(LogoutResult(isLoggedOut = true))
            } catch (ex: Exception) {
                _logoutResult.postValue(LogoutResult(isLoggedOut = false))
            }
        }
    }
}