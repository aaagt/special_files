plugins {
    id("java")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

allprojects {
    group = "specialfiles"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
