import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish") version "0.30.0"

}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "shared"
            isStatic = true
        }
        pod("AWSS3") {
            version = "2.31.0"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("AWSCore") {
            version = "2.31.0"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.aws.android.sdk.s3)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.mohaberabi.s3klient"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.mohaberabi",
        artifactId = "s3-klient",
        version = "0.0.2"
    )

    pom {
        name.set("KMP Library for wrapping aws s3 client for ios and android ")
        description.set("KMP Library for wrapping aws s3 client for ios and android")
        inceptionYear.set("2025")
        url.set("https://github.com/mohaberabi/s3-klient")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("mohaberabi")
                name.set("Mohab Erabi")
                email.set("moohaberabii98@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/mohaberabi/s3-klient")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}