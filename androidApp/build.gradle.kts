
import de.undercouch.gradle.tasks.download.Download
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.crashlytics.plugin)
    alias(libs.plugins.googleplay.plugin)
    alias(libs.plugins.android.download)
    alias(libs.plugins.compose.compiler)
}

tasks {
    fun Download.process(input: String, output: String) {
        src(input)
        dest(output)
        onlyIfModified(true)
        useETag("all")
        outputs.file(output)
    }

    val downloadLocalFr by registering(type = Download::class) {
        @Suppress("ktlint:standard:max-line-length")
        process(
            "https://localise.biz/api/export/locale/fr-FR.xml?format=android&key=RBA2TaeBtFxlFCmVefau01mNb2bPS7eU",
            "src/main/res/values-fr/strings.xml"
        )
    }
    val downloadLocalEn by registering(type = Download::class) {
        @Suppress("ktlint:standard:max-line-length")
        process(
            "https://localise.biz/api/export/locale/en.xml?format=android&key=RBA2TaeBtFxlFCmVefau01mNb2bPS7eU",
            "src/main/res/values/strings.xml"
        )
    }
    configureEach {
        if (this.name.contains("assembleDebug")) {
            this.dependsOn(downloadLocalFr, downloadLocalEn)
        }
        if (this.name.contains("mapDebugSourceSetPaths") ||
            this.name.contains("packageDebugResources") ||
            this.name.contains("mergeDebugResources")
        ) {
            this.inputs.files(downloadLocalFr, downloadLocalEn)
        }
    }
}

android {
    signingConfigs {
        create("release") {
            val signingConfigFile = file("signing.properties")
            val properties = Properties().apply {
                load(signingConfigFile.reader())
            }
            storeFile = File(properties.getProperty("STORE_FILE"))
            storePassword = properties.getProperty("STORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }
    namespace = "com.cacd2.cacdgame.android"
    compileSdk = libs.versions.targetSdk.get().toInt()
    defaultConfig {
        applicationId = "com.cacd2.cacdgame.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 110
        versionName = "1.0.6"
        resourceConfigurations.addAll(listOf("en", "fr"))
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs["release"]
            resValue("bool", "FIREBASE_ANALYTICS_ENABLED", "false")
            resValue("bool", "FIREBASE_CRASHLYTICS_ENABLED", "false")
            buildConfigField(
                "boolean",
                "FOR_TESTING",
                properties["forTesting"]?.toString() ?: "false"
            )
            proguardFiles(
                "proguard-rules.pro",
                getDefaultProguardFile("proguard-android-optimize.txt")
            )
        }
        debug {
            resValue("bool", "FIREBASE_ANALYTICS_ENABLED", "false")
            resValue("bool", "FIREBASE_CRASHLYTICS_ENABLED", "false")
            buildConfigField(
                "boolean",
                "FOR_TESTING",
                "true"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_1_8}"
    }
}

dependencies {
    implementation(projects.shared)
    coreLibraryDesugaring(libs.android.desugar)
    implementation(platform(libs.android.firebase.bom))
    implementation(libs.bundles.android.firebase)
    implementation(platform(libs.android.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.android.compose.navigation)
    implementation(libs.android.splashscreen)
    implementation(libs.bundles.android.koin)
    implementation(libs.android.chart)
    implementation(libs.android.coil)
    implementation(libs.shake)
    implementation(libs.bundles.compose.debug)
}
