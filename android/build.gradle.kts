plugins {
    id("com.android.application") version "8.2.2"
    kotlin("android")
}

android {
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    namespace = "dev.bnorm.elevated"
    defaultConfig {
        applicationId = "dev.bnorm.elevated"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        compose = true
//    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources.excludes += "**/attach_hotspot_windows.dll"
        resources.excludes += "META-INF/licenses/**"
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }

    lint {
        abortOnError = false
    }
}
