package ar.edu.itba.hci.android

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import ar.edu.itba.hci.android.databinding.ActivityLoginBinding


private lateinit var binding: ActivityLoginBinding

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = insets.left,
                right = insets.right,
                top = insets.top,
                bottom = insets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        val color = ResourcesCompat.getColor(resources, R.color.orange_light, null)
        window.statusBarColor = color

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = false
            } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = true
            } // Night mode is active, we're using dark theme
        }
    }
}