plugins {
<<<<<<< HEAD
    id("com.android.application")
=======
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
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
<<<<<<< HEAD
<<<<<<< HEAD
=======

    // Nếu bạn dùng Kotlin 1.9+ hoặc cao hơn, thêm phần này để tương thích tốt hơn
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
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
<<<<<<< HEAD
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
=======
    // Core AndroidX libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
<<<<<<< HEAD
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa

    // jTDS (SQL Server)
    implementation("net.sourceforge.jtds:jtds:1.3.1")
<<<<<<< HEAD

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
=======
<<<<<<< HEAD
}


=======
    
    // JavaMail để gửi email OTP
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}
>>>>>>> 9dad7ec6e3d884d03c02d023d6c9c8c25d7be764
=======

    // Chart library
    implementation(libs.mpandroidchart)

    // JTDS driver for SQL Server
    implementation(libs.jtds)

    // JavaMail for sending email OTP (Android compatible)
    implementation(libs.android.mail)
    implementation(libs.android.activation)
}
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
