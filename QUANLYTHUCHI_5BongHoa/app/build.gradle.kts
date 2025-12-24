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

    implementation(libs.appcompat)
    implementation(libs.material)  // Đã có, nhưng đảm bảo version mới
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Material Components (đã có libs.material, nhưng thêm explicit nếu cần)
    implementation("com.google.android.material:material:1.12.0")

    // Chart (bạn đã có MPAndroidChart)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    
    // JTDS driver để kết nối SQL Server
    implementation("net.sourceforge.jtds:jtds:1.3.1")
    
    // JavaMail để gửi email OTP
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}