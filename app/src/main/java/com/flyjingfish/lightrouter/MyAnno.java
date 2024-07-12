package com.flyjingfish.lightrouter;

import com.flyjingfish.android_aop_annotation.anno.AndroidAopPointCut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AndroidAopPointCut(MyAnnoCut.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnno {
}
