package ar.edu.itba.hci.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.itba.hci.android.databinding.ActivityLoginBinding


private lateinit var binding: ActivityLoginBinding

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}