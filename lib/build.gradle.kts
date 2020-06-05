import java.io.File

plugins {
    `java-library`
}

tasks.test {
    useJUnitPlatform()
}


dependencies {
    compile("org.eclipse.jdt:org.eclipse.jdt.core:3.21.0")
    compile("com.google.code.gson:gson:2.8.6")
    compile("org.jetbrains:annotations:13.0")
    compile("org.parboiled:parboiled-java:1.3.1")

    /*
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    */

    compileOnly("org.projectlombok:lombok:1.18.10")
    annotationProcessor("org.projectlombok:lombok:1.18.10")
}

fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String? = try {
    ProcessBuilder(split("\\s".toRegex()))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectErrorStream(true)
            .start().apply { waitFor(timeoutAmount, timeoutUnit) }
            .inputStream.bufferedReader().readText()?.trim()
} catch (e: java.io.IOException) {
    e.printStackTrace()
    null
}

sourceSets["main"].resources.srcDir(File(buildDir, "generated-resources"))

tasks.register("writeVersionFile") {
    var outputFile: File = File(buildDir, "generated-resources/jml-extract.version")
    outputs.file(outputFile)

    doLast {
        outputFile.parentFile.mkdirs()
        var git = "git describe --tags --all --long".runCommand()
        outputFile.writeText("jml-extract ${project.version} (${git})")
    }
}

tasks["processResources"].dependsOn("writeVersionFile")