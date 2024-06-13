package com.flyjingfish.lightrouter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.flyjingfish.lightrouter.IgnoreIntercept
import com.flyjingfish.lightrouter.databinding.ActivityMainBinding
import com.flyjingfish.login.LoginActivity
import com.flyjingfish.module_communication_intercept.RouterInterceptManager
import com.flyjingfish.module_communication_intercept.intercept.InterceptPoint


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
        binding.btnAddIntercept.setOnClickListener {
            RouterInterceptManager.addIntercept(object :IgnoreIntercept{
                override fun onIntercept(point: InterceptPoint) {
                    Log.e("onIntercept","--MainActivity--${point.path},params = ${point.paramsMap},byPath = ${point.byPath}")
                    point.proceed()
                }

                override fun order(): Int {
                    return 2
                }

            })
            startActivity(Intent(this,ImagesActivity::class.java))
        }
    }
}