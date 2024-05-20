package com.flyjingfish.lightrouter.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.flyjingfish.lightrouter.databinding.ActivityMainBinding
import com.flyjingfish.login.LoginActivity

class MainActivity : ComponentActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}