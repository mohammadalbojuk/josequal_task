// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
    repositories {
        mavenCentral()
    }

}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id ("com.android.library") version "7.4.0" apply false
//    id ("com.google.dagger.hilt.android") version "2.44" apply false
}