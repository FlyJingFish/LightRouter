package com.flyjingfish.login

import android.util.Log
import com.flyjingfish.module_communication_intercept.intercept.Proceed
import com.flyjingfish.module_communication_intercept.intercept.RouterIntercept

class LoginIntercept : RouterIntercept {
    override fun onIntercept(proceed: Proceed) {
        Log.e("onIntercept","--LoginIntercept--${proceed.path},params = ${proceed.paramsMap},byPath = ${proceed.byPath}")
        proceed.proceed()
    }

    override fun order(): Int {
        return 1
    }
}