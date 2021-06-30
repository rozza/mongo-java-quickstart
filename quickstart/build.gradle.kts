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
        mavenCentral()
        jcenter()
    }
}

plugins {
    `java-library`
    scala
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}


dependencies {
//    implementation(fileTree("libs"))
//
//    implementation(platform("io.projectreactor:reactor-bom:Californium-SR23"))
//    implementation("io.projectreactor:reactor-core")
//    implementation("org.reactivestreams:reactive-streams:1.0.3")

    implementation("org.mongodb.scala:mongo-scala-driver_2.13:4.2.3")

    implementation("org.scala-lang:scala-library:2.13.4")
    implementation("org.apache.logging.log4j:log4j-api:2.3")
    implementation("org.apache.logging.log4j:log4j-core:2.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.3")
}

tasks.create("quickStart", JavaExec::class.java) {
    description = "Runs the quickstart"
    main = "quickstart.QuickStart"
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf(System.getProperties().getProperty("connectionString", ""))
}
