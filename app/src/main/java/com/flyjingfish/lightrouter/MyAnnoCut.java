package com.flyjingfish.lightrouter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flyjingfish.android_aop_annotation.ProceedJoinPoint;
import com.flyjingfish.android_aop_annotation.base.BasePointCut;

public class MyAnnoCut implements BasePointCut<MyAnno> {
    @Nullable
    @Override
    public Object invoke(@NonNull ProceedJoinPoint joinPoint, @NonNull MyAnno anno) {
        joinPoint.proceed();
        Log.e("MyAnnoCut","------invoke------");
        return null;
    }
}
