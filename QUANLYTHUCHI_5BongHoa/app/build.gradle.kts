plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.quanlythuchi_5bonghoa"
    compileSdk = 36  // Hoặc 34/35 nếu máy bạn chưa tải SDK 36

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
}

dependencies {
    // AndroidX core
    implementation(libs.appcompat)
    implementation(libs.material)  // Đã có, nhưng đảm bảo version mới
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Material Components (đã có libs.material, nhưng thêm explicit nếu cần)
    implementation("com.google.android.material:material:1.12.0")

    // Chart (bạn đã có MPAndroidChart)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // JTDS cho kết nối SQL Server (đã có)
    implementation("net.sourceforge.jtds:jtds:1.3.1")

    // Nếu bạn dùng Room (khuyến nghị thay SQLite thủ công)
    // implementation("androidx.room:room-runtime:2.6.1")
    // annotationProcessor("androidx.room:room-compiler:2.6.1")
    // implementation("androidx.room:room-ktx:2.6.1")  // Nếu dùng Kotlin

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}