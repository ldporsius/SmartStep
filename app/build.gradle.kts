import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id("com.google.gms.google-services")

}

android {
    namespace = "nl.codingwithlinda.smartstep"
    compileSdk = 36

    defaultConfig {
        applicationId = "nl.codingwithlinda.smartstep"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "GEMINI_AI_KEY", properties.getProperty("GEMINI_AI_KEY"))

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
   
    buildFeatures {
        compose = true
        buildConfig = true
    }
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("11")
    }
}
room {
    schemaDirectory("$projectDir/schemas")
}
dependencies {

    implementation(project(":ai_integration"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:2.10.0")
    implementation("androidx.datastore:datastore-preferences:1.2.0")
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.core.splashscreen)

    //room
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(project(":core"))
    implementation(project(":ai_firebase"))

    ksp(libs.androidx.room.compiler)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    //constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //ai
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-ai")
    implementation("com.google.firebase:firebase-config")
    //jvm tests
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.assertk)

    //instrumented/ui tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.assertk)
    androidTestUtil(libs.androidx.orchestrator)

    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)

}