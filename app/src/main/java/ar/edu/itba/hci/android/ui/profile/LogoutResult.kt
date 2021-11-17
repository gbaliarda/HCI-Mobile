package ar.edu.itba.hci.android.ui.profile

/**
 * Authentication result : success (user details) or error message.
 */
data class LogoutResult(
    val isLoggedOut: Boolean = false,
)