plugins {
    java
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "br.com.act"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://jasperreports.sourceforge.net/maven2/") }
    maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/") }
    mavenLocal()
}

extra["springCloudVersion"] = "2022.0.1"
val actPlatformVersion = "1.0.4"
val jasperVersion = "6.20.0"
val springDocVersion = "2.1.0"

dependencies {
    implementation("br.com.act:act-platform:${actPlatformVersion}")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${springDocVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra-reactive")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Report
    implementation("net.sf.jasperreports:jasperreports-metadata:${jasperVersion}")
    implementation("net.sf.jasperreports:jasperreports-functions:${jasperVersion}")
    implementation("net.sf.jasperreports:jasperreports-fonts:${jasperVersion}")
    implementation("net.sf.jasperreports:jasperreports:${jasperVersion}")
    implementation("xerces:xercesImpl:2.12.2")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.kafka:spring-kafka")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
