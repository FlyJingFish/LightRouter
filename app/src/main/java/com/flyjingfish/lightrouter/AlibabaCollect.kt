package com.flyjingfish.lightrouter

import android.util.Log
import com.alibaba.android.arouter.facade.template.IInterceptorGroup
import com.alibaba.android.arouter.facade.template.IProviderGroup
import com.alibaba.android.arouter.facade.template.IRouteRoot
import com.flyjingfish.android_aop_annotation.anno.AndroidAopCollectMethod
import com.flyjingfish.android_aop_annotation.enums.CollectType

object AlibabaCollect {
    private val classNameSet = mutableSetOf<String>()

    @AndroidAopCollectMethod(
        regex = "com.alibaba.android.arouter.routes.*?",
        collectType = CollectType.DIRECT_EXTENDS
    )
    @JvmStatic
    fun collectIRouteRoot(sub: Class<out IRouteRoot>){
        Log.e("AlibabaCollect","collectIRouteRoot=$sub")
        classNameSet.add(sub.name)
    }

    @AndroidAopCollectMethod(
        regex = "com.alibaba.android.arouter.routes.*?",
        collectType = CollectType.DIRECT_EXTENDS
    )
    @JvmStatic
    fun collectIProviderGroup(sub: Class<out IProviderGroup>){
        Log.e("AlibabaCollect","collectIProviderGroup=$sub")
        classNameSet.add(sub.name)
    }

    @AndroidAopCollectMethod(
        regex = "com.alibaba.android.arouter.routes.*?",
        collectType = CollectType.DIRECT_EXTENDS
    )
    @JvmStatic
    fun collectIInterceptorGroup(sub: Class<out IInterceptorGroup>){
        Log.e("AlibabaCollect","collectIInterceptorGroup=$sub")
        classNameSet.add(sub.name)
    }

    fun getClassNameSet() :MutableSet<String>{
        return classNameSet
    }
}