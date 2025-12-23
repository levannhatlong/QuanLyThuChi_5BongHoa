plugins {
    alias(libs.plugins.android.application)
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
<<<<<<< HEAD
    
    // JTDS driver để kết nối SQL Server
    implementation("net.sourceforge.jtds:jtds:1.3.1")
}
=======
<<<<<<< HEAD

    // Thêm thư viện JTDS để kết nối SQL Server
    implementation("net.sourceforge.jtds:jtds:1.3.1")
}
=======
    
    // JTDS driver để kết nối SQL Server
    implementation("net.sourceforge.jtds:jtds:1.3.1")
}
>>>>>>> HoThiMyHa
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
