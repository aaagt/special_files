plugins {
    application
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

application {
    mainClass.set("Main")
}

sourceSets {
    main {
        java.srcDir("${project.path}/src/${this@main.name}/${this.name}")
    }
    test {
        java.srcDir("${project.path}/src/${this@test.name}/${this.name}")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
