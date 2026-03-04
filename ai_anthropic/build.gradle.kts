import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.runtimeOnly

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "nl.codingwithlinda.ai_anthropic"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // import Kotlin API client BOM
    implementation (platform("com.aallam.openai:openai-client-bom:4.1.0"))

    // define dependencies without versions
    implementation ("com.aallam.openai:openai-client")
    runtimeOnly ("io.ktor:ktor-client-okhttp")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}