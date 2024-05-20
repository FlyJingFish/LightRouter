plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("communication.module")
}
apply("$rootDir/gradle/android_base.gradle")
android {
    namespace = "com.flyjingfish.communication"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

//    sourceSets {
//        this.getByName("debug").res.srcDirs("src/main/res")
//    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
}