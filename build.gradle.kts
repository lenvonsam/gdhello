import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.9.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	id("com.bmuschko.docker-remote-api") version "5.2.0"
	war
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
}

group = "sam.trial"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

docker {
	url.set("tcp://192.168.80.200:2375")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

val copyJarToDocker by tasks.creating(Copy::class) {
	dependsOn(tasks.bootJar)
	from("build/libs")
	into("src/main/docker")
	include("${project.name}-${project.version}.jar")
}

val buildGradleDockerImage by tasks.creating(DockerBuildImage::class) {
	dependsOn(copyJarToDocker)
	inputDir.set(file("src/main/docker"))
	tags.add("${project.name}:0.0.1")
}

