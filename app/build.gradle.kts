plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Penting untuk Room
}

android {
    namespace = "com.example.appdaftarkontak"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.appdaftarkontak"
        minSdk = 29 // Bisa disesuaikan, tapi 29 sudah cukup kompatibel
        targetSdk = 35
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Pastikan versi ini kompatibel dengan Compose BOM Anda
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Dependensi Compose default
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // --- Dependensi Tambahan untuk Aplikasi CRUD (Kotlin dan Room) ---

    // Room Components (Database Persistence Library)
    val room_version = "2.6.1" // Periksa versi terbaru di developer.android.com/jetpack/androidx/releases/room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version") // Untuk Kotlin annotation processing
    implementation("androidx.room:room-ktx:$room_version") // Sudah ditambahkan sebelumnya

    // Kotlin Coroutines (untuk operasi asinkron non-blocking)
    val coroutines_version = "1.7.3" // Periksa versi terbaru di github.com/Kotlin/kotlinx.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // ViewModel dan LiveData (Komponen Arsitektur Android Jetpack)
    val lifecycle_version = "2.7.0" // Periksa versi terbaru di developer.android.com/jetpack/androidx/releases/lifecycle
    // Untuk menggunakan ViewModel dengan Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // asLiveData() memerlukan ini
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Untuk mengamati LiveData sebagai State di Compose (menambahkan observeAsState)
    val compose_livedata_version = "1.6.8" // Pastikan ini sesuai atau coba 1.6.8
    implementation("androidx.compose.runtime:runtime-livedata:$compose_livedata_version") // <<< BARIS PENTING INI

    // Icons Extended (untuk ikon tambahan di Compose, misal Icons.Filled.Add)
    val icons_extended_version = "1.6.8" // Contoh versi, bisa disesuaikan atau pakai versi dari composeBom
    implementation("androidx.compose.material:material-icons-extended:$icons_extended_version")

    // Dependensi Tambahan untuk Navigasi Compose
    val nav_version = "2.7.7" // Periksa versi terbaru di developer.android.com/jetpack/androidx/releases/navigation
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // --- Akhir Dependensi Tambahan ---

    // Dependensi testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}