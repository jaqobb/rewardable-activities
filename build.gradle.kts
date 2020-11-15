plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

group = "dev.jaqobb"
version = "1.0.0"
description = "Spigot plugin that rewards players when they do certain activities"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

bukkit {
    name = "PaidActivities"
    main = "dev.jaqobb.paidactivities.PaidActivitiesPlugin"
    version = project.version as String
    depend = listOf("Vault")
    description = project.description
    author = "jaqobb"
    website = "https://jaqobb.dev"
}

repositories {
    jcenter()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        content {
            includeGroup("org.spigotmc")
        }
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots/") {
        content {
            includeGroup("net.md-5")

        }
    }
    maven("http://nexus.hc.to/content/repositories/pub_releases/") {
        content {
            includeGroup("net.milkbowl.vault")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
}
