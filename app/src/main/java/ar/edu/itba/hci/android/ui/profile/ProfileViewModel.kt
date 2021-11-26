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

    private val _profileResult: MutableLiveData<ProfileResult> by lazy {
        MutableLiveData<ProfileResult>().also {
            refreshProfile()
        }
    }
    val profileResult: MutableLiveData<ProfileResult> = _profileResult

    fun refreshProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currUser = userRepository.getCurrentUser()
                _profileResult.postValue(ProfileResult(name = currUser.firstName, email = currUser.email, avatarUrl = currUser.avatarUrl))
            } catch(ex:Exception) {
                _profileResult.postValue(ProfileResult(name = null, email = null))
                println("Error al cargar perfil ${ex.stackTrace}")
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