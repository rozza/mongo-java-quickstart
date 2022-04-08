/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}


dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.6.0-SNAPSHOT")
    implementation("org.apache.logging.log4j:log4j-api:2.3")
    implementation("org.apache.logging.log4j:log4j-core:2.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.3")
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main"))
        }
    }
}

tasks.compileJava {
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = false
}

tasks.create("quickStart", JavaExec::class.java) {
    description = "Runs the quickstart"
    main = "quickstart.QuickStart"
    classpath = sourceSets["main"].runtimeClasspath
    args = System.getProperties().getProperty("connectionString", "").split(",")
}
