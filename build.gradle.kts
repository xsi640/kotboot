val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val hikaricp_version: String by project
val koin_version: String by project
val classgraph_version: String by project
val commons_io_version: String by project
val commons_codec_version: String by project
val commons_lang_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
}

allprojects {

    apply {
        plugin("kotlin")
        plugin("org.gradle.application")
    }

    group = "com.example"
    version = "0.0.1"

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
            url = uri("http://172.16.11.231:8081/nexus/repository/maven2-group/")
        }
    }

    dependencies {
        implementation("io.ktor:ktor-jackson:$ktor_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-jodatime:$exposed_version")
        implementation("io.insert-koin:koin-core:$koin_version")
        implementation("org.postgresql:postgresql:$postgresql_version")
        implementation("com.zaxxer:HikariCP:$hikaricp_version")
        implementation("io.github.classgraph:classgraph:$classgraph_version")

        implementation("commons-io:commons-io:$commons_io_version")
        implementation("commons-codec:commons-codec:$commons_codec_version")
        implementation("org.apache.commons:commons-lang3:$commons_lang_version")

        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    }
}