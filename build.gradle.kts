plugins {
    `java-library`
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "eu.letsmine.bluemap"
version = "1.1"

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
    implementation("com.pastdev:jsch-nio:1.0.14")
}

tasks.shadowJar {
    //relocate( "com.pastdev", "eu.letsmine.bluemap.shadow.jsch-nio" )

    doLast {
        destinationDirectory.file(archiveFileName).get().asFile
            .copyTo(projectDir.resolve("build/release/bluemap-sshstorage-$version.jar"))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.encoding = "utf-8"
}

tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}
