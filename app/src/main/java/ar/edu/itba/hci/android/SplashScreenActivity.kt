package ar.edu.itba.hci.android

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

import android.os.Build
import android.os.Looper
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import org.w3c.dom.Attr


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler:Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val origStatusColor = window.statusBarColor
        window.statusBarColor = typedValue.data

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            window.statusBarColor = origStatusColor

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}