plugins {
}

allprojects {
    group = "jml.extractor"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "maven-publish")
    configure<> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/wadoon/jml-extract")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("PASSWORD")
                }
            }
        }
        publications {
            register("gpr") {
                from(components["java"])
            }
        }
    }
}
