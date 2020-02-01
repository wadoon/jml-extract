import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    //id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    `java-library`
    `application`
}

dependencies {
    implementation("org.docopt:docopt:0.6.0")
    implementation(project(":lib"))
}
configure<ApplicationPluginConvention> {
    mainClassName = "jml.Main"
}

configure<ShadowExtension> {
    applicationDistribution.from("src/dist")
}

tasks.named<ShadowJar>("shadowJar") {
    minimize()
}
