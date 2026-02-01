plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.9.23"
}

android {
    namespace = "com.example.testwxy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testwxy"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
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
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // 改为 17
        targetCompatibility = JavaVersion.VERSION_17 // 改为 17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir}/compose_compiler",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir}/compose_compiler"
        )
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    //使用版本目录中的 composeCompiler 版本
    composeOptions {
        // 使用版本目录中的配置
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }


    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/gradle/incremental.annotation.processors",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt"
            )
        }
    }
}


dependencies {

    // =========== 基础库（核心+UI组件） ===========
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.bundles.ui.components) // 1行替代3行（constraintlayout+recyclerview+viewpager2）

    // =========== ViewModel/Lifecycle ===========
    implementation(libs.bundles.lifecycle) // 1行替代3行
    implementation(libs.androidx.lifecycle.viewmodel.compose) // 单独引用（无Bundle）

    // =========== Navigation ===========
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    // =========== Compose ===========
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose) // 1行替代7行（ui/graphics/material3等）
    implementation(libs.bundles.compose.integration) // 1行替代3行（activity-compose/navigation-compose等）
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime.livedata)

    // =========== 网络 ===========
    implementation(libs.bundles.network) // 1行替代6行（retrofit/okhttp/gson等）
    implementation(libs.bundles.network.rxjava) // 1行替代3行

    // =========== 图片 ===========
    implementation(libs.bundles.image.loading)
    kapt(libs.bundles.image.loading.processor) // 1行替代1行

    // =========== 数据库 ===========
    implementation(libs.bundles.room) // 1行替代2行
    kapt(libs.bundles.room.processor) // 1行替代1行

    // =========== Desugaring（升级到兼容 compileSdk 34 的版本） ===========
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

    // =========== 测试 ===========
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}