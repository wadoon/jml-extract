plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    //id("org.jetbrains.kotlin.jvm") version "1.3.61"
    // Apply the application plugin to add support for building a CLI application.
    application
    antlr
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.20.0")
    implementation("com.google.code.gson:gson:2.8.6")

    /*
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    */

    implementation("org.antlr:antlr4:4.7.2")
    antlr("org.antlr:antlr4:4.7.2")
    compileOnly("org.projectlombok:lombok:1.18.10")
    annotationProcessor("org.projectlombok:lombok:1.18.10")

}

application {
    mainClassName = "jml.annotation.Main"
}
