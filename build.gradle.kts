import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

allprojects {
    apply {
        plugin("kotlin")
    }

    group = "com.github.xsi640"
    version = "0.0.1"

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    val user = System.getProperty("repoUser")
    val pwd = System.getProperty("repoPassword")

    repositories {
        mavenLocal()
        maven {
            credentials {
                username = user
                password = pwd
                isAllowInsecureProtocol = true
            }
            url = uri("http://nexus.suyang.home/repository/maven-group/")
        }
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}