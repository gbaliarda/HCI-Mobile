package ar.edu.itba.hci.android.ui.profile

/**
 * Authentication result : success (user details) or error message.
 */
data class ProfileResult(
    val name: String? = null,
    val email: String? = null,
    val avatarUrl: String ? = null
)