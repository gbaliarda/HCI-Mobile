package ar.edu.itba.hci.android

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context:Context) {
    companion object {
        private const val AUTH_TOKEN = "auth_token"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    var authToken:String?
        get() = sharedPreferences.getString(AUTH_TOKEN, null)
        set(value) {
            with(sharedPreferences.edit()) {
                putString(AUTH_TOKEN, value)
                apply()
            }
        }
}
