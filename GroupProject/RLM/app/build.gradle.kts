plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
//    id("org.jetbrains.kotlin.kapt") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    namespace = "edu.qc.seclass.rlm"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.qc.seclass.rlm"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions{
        jvmTarget = "1.8"
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}
ksp {
    arg("jvmTarget", "1.8")
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("org.mockito:mockito-core:3.x")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation(kotlin("stdlib-jdk8"))

    val room_version = "2.6.0" // Use the latest version compatible with KSP

    // Room components with KSP
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Optional - Test helpers for Room
    testImplementation("androidx.room:room-testing:$room_version")

    // Optional - Paging 3 Integration for Room
    implementation("androidx.room:room-paging:$room_version")
}

