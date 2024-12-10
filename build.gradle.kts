plugins {
	java
	war
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Security Starter
	implementation("org.springframework.boot:spring-boot-starter-security")

	// Spring Web Starter
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Spring Data JPA (para interactuar con bases de datos)
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// MySQL Driver (para la base de datos MySQL)
	runtimeOnly("mysql:mysql-connector-java")

	// Spring Boot DevTools (solo en desarrollo)
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Spring Configuration Processor
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// Tomcat embebido
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

	// OAuth2 Resource Server (para implementar un servidor de recursos con OAuth2)
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	// Testing: Spring Boot y Spring Security
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// JUnit Launcher (Test Runtime)
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
