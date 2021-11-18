package ar.edu.itba.hci.android.ui.login

import android.util.Patterns
import androidx.lifecycle.*
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.data.LoginRepository
import ar.edu.itba.hci.android.data.Result

import ar.edu.itba.hci.android.R
import ar.edu.itba.hci.android.api.model.ApiError
import ar.edu.itba.hci.android.api.model.Credentials
import ar.edu.itba.hci.android.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val application: MainApplication
    ) : ViewModel() {

    private val userRepository = application.userRepository

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun checkToken() {
        application.preferences.authToken?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val user = userRepository.getCurrentUser()
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = user.firstName!!)))
                }
                catch (ex:Exception) {
                    application.preferences.authToken = null
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = userRepository.login(Credentials(username, password))
                application.preferences.authToken = result.token

                val user = userRepository.getCurrentUser()
                _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = user.firstName!!)))
            }
            catch (ex:Exception) {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}