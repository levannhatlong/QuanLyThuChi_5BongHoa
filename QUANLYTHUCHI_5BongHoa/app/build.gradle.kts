plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Plugin KSP đã được xóa vì không còn sử dụng Room
}

android {
    namespace = "com.example.quanlythuchi_5bonghoa"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.quanlythuchi_5bonghoa"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Các thư viện Room đã được xóa bỏ

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Thêm thư viện JTDS để kết nối SQL Server
    implementation("net.sourceforge.jtds:jtds:1.3.1")
}
