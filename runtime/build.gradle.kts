import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    // JS is the runnable reference target. Native targets reserve the platform adapter boundary.
    linuxX64()
    linuxArm64()
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "portable-droid.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(static = mutableListOf(file("src/jsMain/resources").absolutePath))
            }
        }
        binaries.executable()
    }
    sourceSets {
        jsMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.html.core)
        }
    }
}

tasks.register<Copy>("assemblePwa") {
    group = "distribution"
    description = "Assembles browser resources and the Kotlin/JS bundle as a PWA directory."
    dependsOn("jsBrowserDistribution")
    from(layout.buildDirectory.dir("kotlin-webpack/js/productionExecutable"))
    from("src/jsMain/resources")
    into(layout.buildDirectory.dir("pwa"))
}
