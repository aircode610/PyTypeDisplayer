plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.intellij)
}

group = "com.amirali.myplugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set(libs.versions.intellij.version.get())
    type.set("PY") // PyCharm Professional

    plugins.set(listOf("Pythonid"))

    downloadSources.set(false)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = libs.versions.java.version.get()
        targetCompatibility = libs.versions.java.version.get()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = libs.versions.java.version.get()
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    test {
        enabled = false
    }
}