plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.2"
}

group = "eu.letsmine.bluemap"
version = "0.2"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven {
        name = "bluecoloredReleases"
        url = uri("https://repo.bluecolored.de/releases")
    }
}

dependencies {
    compileOnly ( "de.bluecolored.bluemap:BlueMapCore:5.3" )
    compileOnly ( "de.bluecolored.bluemap:BlueMapCommon:5.3" )
    implementation("com.github.robtimus:sftp-fs:3.3.1")
}

tasks.shadowJar {
    relocate( "com.jcraft.jsch", "eu.letsmine.bluemap.shadow.com.jcraft.jsch" )
    relocate( "com.github.robtimus.filesystems", "eu.letsmine.bluemap.shadow.com.github.robtimus.filesystems" )
    relocate( "com.github.robtimus.pool", "eu.letsmine.bluemap.shadow.com.github.robtimus.pool" )
    doLast {
        destinationDirectory.file(archiveFileName).get().asFile
            .copyTo(projectDir.resolve("build/release/letsmine-bluemap-$version.jar"))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.encoding = "utf-8"
}

tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}