import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.41"
	kotlin("plugin.spring") version "1.3.41"
	id("org.jetbrains.kotlin.plugin.noarg") version "1.3.41"
}

noArg {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
}

group = "com.heycar"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core:5.2.4")
	runtime("org.postgresql:postgresql:42.2.6")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.h2database:h2:1.4.199")
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
	testImplementation("com.squareup.okhttp3:okhttp:3.14.2")
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform { }
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}