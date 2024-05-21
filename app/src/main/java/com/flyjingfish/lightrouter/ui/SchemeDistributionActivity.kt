package com.flyjingfish.lightrouter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.flyjingfish.module_communication_route.ModuleRoute

class SchemeDistributionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        uri?.let {
            ModuleRoute.builder(it)?.go()
        }
        finish()
    }
}