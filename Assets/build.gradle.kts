plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "mystic"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.17")
    implementation("org.apache.xmlgraphics:batik-codec:1.17")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("mystic.assets.MainKt")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("brand.config", "$projectDir/brand.yaml")
    systemProperty("output.dir", "$projectDir/output")
}

tasks.named<JavaExec>("run") {
    args = listOf(
        "--config", "$projectDir/brand.yaml",
        "--output", "$projectDir/output"
    )
}

kotlin {
    jvmToolchain(17)
}
