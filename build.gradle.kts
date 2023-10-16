plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("plugin.lombok") version "1.9.10"
	id("io.freefair.lombok") version "8.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-security:3.1.4")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.4")
//	implementation("org.springframework.security:spring-security-oauth2-resource-server:5.5.0")
	testImplementation("org.springframework.security:spring-security-test:6.1.4")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	compileOnly("org.projectlombok:lombok:1.18.20")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
