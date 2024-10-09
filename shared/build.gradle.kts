import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight.library)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.kotlin.apollo)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.crashkios)
    alias(libs.plugins.kotlin.parcelize)
}

kotlin {

    // Android
    androidTarget {
        // https://youtrack.jetbrains.com/issue/KT-66448
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.cacd2.cacdgame.CommonParcelize"
            )
        }
    }
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "CACDGAME"
        homepage = "les deps iOS"
        version = "1.2"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "Shared"
            binaryOption("bundleId", "com.cacd2.cacdgame.shared")
            export(libs.ios.kermit.simple)
            isStatic = true
        }
        pod("FirebaseCore") {
            version = libs.versions.iosFirebase.get()
        }
        pod("FirebaseAnalytics") {
            version = libs.versions.iosFirebase.get()
        }
        pod("FirebaseCrashlytics") {
            version = libs.versions.iosFirebase.get()
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("MatomoTracker") {
            version = libs.versions.matomoIOSVersion.get()
            extraOpts = listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        sourceSets["commonMain"].kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        commonMain.dependencies {
            implementation(libs.bundles.kmm.kermit)
            implementation(libs.kmm.crashkios)
            implementation(libs.kmm.coroutines)
            implementation(libs.bundles.kmm.ktor)
            implementation(libs.bundles.kmm.sqldelight)
            implementation(libs.bundles.kmm.apollo)
            implementation(libs.bundles.kmm.koin)
            implementation(libs.kmm.collection)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            api(libs.kmm.kermit)
            implementation(libs.android.ktor.client)
            implementation(libs.android.sqldelight.driver)
            implementation(project.dependencies.platform(libs.android.firebase.bom.get()))
            implementation(libs.bundles.android.firebase)
            implementation(libs.bundles.android.koin)
            implementation(libs.bundles.kmm.koin)
            implementation(libs.android.matomo)
            implementation(libs.android.lifecycle)
        }
        iosMain.dependencies {
            api(libs.ios.kermit.simple)
            implementation(libs.ios.ktor.client)
            implementation(libs.ios.sqldelight.driver)
            implementation(libs.bundles.kmm.koin)
        }
    }
}

android {
    namespace = "com.cacd2.cacdgame.android.shared"
    compileSdk = libs.versions.targetSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.kmm.koin.ksp)
}

tasks.withType<KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

sqldelight {
    databases {
        // new wrapper
        create("AppDatabase") {
            packageName.set("com.cacd2.cacdgame.database")
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.cacd2.cacdgame.graphql")
    }
}
