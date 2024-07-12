plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}
apply("$rootDir/gradle/android_base.gradle")
android {
    namespace = "com.flyjingfish.base_lib"
    compileSdk = 34

}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    api(libs.androidAop.annotation)
//    api(libs.androidAop.core)
    api(libs.module.communication.annotation)
    api(libs.module.communication.intercept)
    api(libs.module.communication.route)
}