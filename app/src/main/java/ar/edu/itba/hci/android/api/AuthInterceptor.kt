package ar.edu.itba.hci.android.api

import android.app.Application
import ar.edu.itba.hci.android.MainApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(application: MainApplication) : Interceptor {
    private val prefs = application.preferences

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        prefs.authToken?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}
