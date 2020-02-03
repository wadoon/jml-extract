import java.io.File

plugins {
    `java-library`
    antlr
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages", "-package", "jml")
    outputDirectory = File(buildDir.toString() + "/generated-src/antlr/main/jml/")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    compile("org.eclipse.jdt:org.eclipse.jdt.core:3.20.0")
    compile("com.google.code.gson:gson:2.8.6")
    compile("org.jetbrains:annotations:13.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    compileOnly("org.jetbrains:annotations:16.0.2")
    /*
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    */

    compile("org.antlr:antlr4-runtime:4.7.2")
    antlr("org.antlr:antlr4:4.7.2")
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