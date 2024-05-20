// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.module.communication.plugin)
    }
}
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.androidAop.plugin) apply true
}
true // Needed to make the Suppress annotation work for the plugins block

ext {
    set("sdkVersion",31)
    set("minSdkVersion",21)
}