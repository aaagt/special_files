plugins {
    id("java")
}


dependencies {
    implementation("com.google.code.gson:gson:2.8.2")

    // test
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

tasks.test {
    useJUnitPlatform()
}
