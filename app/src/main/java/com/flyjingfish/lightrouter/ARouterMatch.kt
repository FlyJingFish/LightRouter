package com.flyjingfish.lightrouter

import android.util.Log
import com.alibaba.android.arouter.core.LogisticsCenter
import com.flyjingfish.android_aop_annotation.ProceedJoinPoint
import com.flyjingfish.android_aop_annotation.anno.AndroidAopMatchClassMethod
import com.flyjingfish.android_aop_annotation.base.MatchClassMethod
import com.flyjingfish.android_aop_annotation.enums.MatchType

@AndroidAopMatchClassMethod(
    targetClassName = "com.alibaba.android.arouter.core.LogisticsCenter",
    methodName = ["loadRouterMap"],
    type = MatchType.SELF
)
class ARouterMatch : MatchClassMethod {
    override fun invoke(joinPoint: ProceedJoinPoint, methodName: String): Any? {
        val any = joinPoint.proceed()
        val registerMethod = LogisticsCenter::class.java.getDeclaredMethod("register",java.lang.String::class.java)
        registerMethod.isAccessible = true
        val classNameSet = AlibabaCollect.getClassNameSet()

        Log.e("ARouterMatch","invoke: ${classNameSet.size}")
        classNameSet.forEach {
            registerMethod.invoke(null,it)
            Log.e("ARouterMatch","registerMethod=$it")
        }
        return any
    }
}