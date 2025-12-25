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
<<<<<<< HEAD
    kotlinOptions {
        jvmTarget = "11"
=======
    
    packaging {
        resources {
            excludes += listOf(
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE.txt"
            )
        }
>>>>>>> 9dad7ec6e3d884d03c02d023d6c9c8c25d7be764
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
<<<<<<< HEAD
}


=======
    
    // JavaMail để gửi email OTP
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}
>>>>>>> 9dad7ec6e3d884d03c02d023d6c9c8c25d7be764
