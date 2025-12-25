plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.quanlythuchi_5bonghoa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quanlythuchi_5bonghoa"
        minSdk = 24
        targetSdk = 34  // Nếu bạn đã tải SDK 35/36 thì có thể nâng lên
        versionCode = 1
        versionName = "1.0"

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Nếu bạn dùng Kotlin 1.9+ hoặc cao hơn, thêm phần này để tương thích tốt hơn
    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE.txt"
            )
        }
    }
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Chart library
    implementation(libs.mpandroidchart)

    // JTDS driver for SQL Server
    implementation(libs.jtds)

    // JavaMail for sending email OTP (Android compatible)
    implementation(libs.android.mail)
    implementation(libs.android.activation)
}