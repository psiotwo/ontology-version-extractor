plugins {
    java
    application
    `maven-publish`
    id("io.freefair.lombok") version "8.0.1"
}

group = "com.github.psiotwo"
version = "0.0.2-SNAPSHOT"

application {
    mainClass.set("cz.sio2.obo.commands.CLI")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(libs.slf4j)
    implementation(libs.slf4jsimple)
    implementation(libs.picocli)
    implementation(libs.httpclient5)
    implementation(libs.jena)
    implementation(libs.freemarker)
    implementation(libs.jacksonyaml)

    testImplementation(libs.bundles.testDependencies)
    testRuntimeOnly(libs.bundles.testRuntimeOnly)
}

tasks {
    test {
        useJUnitPlatform()
    }

    named<JavaExec>("run") {
        systemProperties = System.getProperties() as Map<String,Any?>
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.github.psiotwo"
            artifactId = "ontology-version-extractor"
            version = "0.0.2-SNAPSHOT"
            from(components["java"])
        }
    }
}