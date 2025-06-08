plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.todo_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todo_app"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // For ViewModel scope and AndroidViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0") // Consider updating to 2.8.0

    // For LiveData and Transformations.switchMap
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1") // **Update to 2.8.0 if you're on 2.6.2**

    // For viewModels() delegate in Activity
    implementation ("androidx.activity:activity-ktx:1.9.0") // Consider updating to 1.9.0

    // For Room
    implementation ("androidx.room:room-runtime:2.6.1") // Latest stable
    kapt ("androidx.room:room-compiler:2.6.1") // Latest stable
    implementation ("androidx.room:room-ktx:2.6.1") // For Kotlin Coroutines support with Room

    // Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0") // Latest stable
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0") // Latest stable
}
