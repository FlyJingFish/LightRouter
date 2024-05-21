package com.flyjingfish.lightrouter.ui

import android.content.Intent
import android.net.Uri
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
        binding.btnWeb.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://flyjingfish.github.io/demoweb/indexUri.html")
            if (intent.resolveActivity(packageManager) != null) {
                val componentName = intent.resolveActivity(packageManager)
                startActivity(Intent.createChooser(intent, "请选择浏览器"))
            }
        }
    }
}