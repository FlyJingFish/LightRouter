@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("android.aop")
}

android {
    namespace = "com.flyjingfish.lightrouter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.flyjingfish.lightrouter"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["AROUTER_MODULE_NAME"] = project.name
            }
        }
    }
    signingConfigs {
        create("release"){
            storeFile = file(rootProject.file("keystore"))
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.appcompat)
    implementation("io.github.FlyJingFish.OpenImage:OpenImageGlideLib:2.2.2")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(project(mapOf("path" to ":base-lib")))
    implementation(project(mapOf("path" to ":lib-user")))
    implementation(project(mapOf("path" to ":lib-login")))
    ksp(libs.androidAop.ksp)

    implementation("com.alibaba:arouter-api:1.5.2")
    annotationProcessor("com.alibaba:arouter-compiler:1.5.2")
}