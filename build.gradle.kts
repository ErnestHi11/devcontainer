/* SPDX-License-Identifier: Apache-2.0 */
val jarPath = "$rootDir/jars"

plugins {
    kotlin("jvm") version "1.9.20"
    id("com.diffplug.spotless") version "6.21.0"
    id("jvm-test-suite")
    id("com.adarshr.test-logger") version "4.0.0"
}

repositories {
    mavenCentral {
        mavenContent {
            releasesOnly()
        }
    }
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}

dependencies {
    implementation("com.atlan:atlan-java:+")
    implementation("com.atlan:package-toolkit-runtime:+")
    implementation("com.atlan:package-toolkit-config:+")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.20.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    testImplementation("com.atlan:package-toolkit-testing:+")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
}

tasks.create<JavaExec>("customPkgCfg") {
    dependsOn(tasks.build)
    mainClass.set("PackageConfig")
    classpath = sourceSets.main.get().runtimeClasspath
    args = listOf(
        "config",
        "src/main/kotlin",
    )
}

tasks.create<JavaExec>("customPkgGen") {
    dependsOn(tasks.getByName("customPkgCfg"))
    mainClass.set("PackageConfig")
    classpath = sourceSets.main.get().runtimeClasspath
    workingDir = rootDir
    args = listOf(
        "package",
        "generated-packages",
    )
}

tasks.create("customPkg") {
    dependsOn(tasks.getByName("customPkgGen"))
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useTestNG()
            targets {
                all {
                    testTask.configure {
                        testLogging.showStandardStreams = true
                    }
                }
            }
            sources {
                java {
                    setSrcDirs(listOf("src/test/kotlin"))
                }
            }
        }
    }
}

tasks {
    jar {
        destinationDirectory.set(file(jarPath))
    }
    test {
        useTestNG()
        onlyIf {
            project.hasProperty("packageTests")
        }
    }
    clean {
        delete(jarPath)
    }
}

kotlin {
    jvmToolchain(17)
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
