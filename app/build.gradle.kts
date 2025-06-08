plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.protobuf")
    kotlin("plugin.serialization") version "2.0.0"
}

android {

    namespace = "com.example.workmanagertest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.workmanagertest"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 35
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
    buildFeatures {
        compose = true
    }
    android.sourceSets["main"].java.srcDirs("src/main/java", "src/main/proto")
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("com.google.protobuf:protobuf-javalite:3.21.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(kotlin("reflect"))
    implementation("com.squareup.moshi:moshi:1.15.0")
    // Kotlin テスト用
    testImplementation ("org.jetbrains.kotlin:kotlin-test:1.9.10")
// JUnit（Kotlinでの標準テスト）
    testImplementation ("junit:junit:4.13.2")

// MockK（モックライブラリ）
    testImplementation ("io.mockk:mockk:1.13.5")

// kotlinx-coroutines-test（runTest などを使う場合）
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

// kotlinx-serialization（JSON文字列操作のため）
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
