plugins {
    kotlin("jvm")
    kotlin("plugin.serialization").version(Versions.Kotlin.lang)
}

java {
    sourceCompatibility = Versions.Java.java
    targetCompatibility = Versions.Java.java
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    kotlinCoroutines()
    kotlinSerialization()
    hson()
    retrofit()
    okhttp()
    junit5()
    api("joda-time:joda-time:${Versions.jodaTime}")
}
