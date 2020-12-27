plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "dev.jaqobb"
version = "1.8.1-SNAPSHOT"
description = "Spigot plugin that rewards players when they do certain activities"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

bukkit {
    name = "RewardableActivities"
    main = "dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin"
    version = project.version as String
    apiVersion = "1.13"
    softDepend = listOf("Vault")
    description = project.description
    author = "jaqobb"
    website = "https://jaqobb.dev"
}

tasks {
    shadowJar {
        exclude("com/cryptomorin/xseries/messages/*")
        exclude("com/cryptomorin/xseries/particles/*")
        exclude("com/cryptomorin/xseries/NMSExtras*")
        exclude("com/cryptomorin/xseries/NoteBlockMusic*")
        exclude("com/cryptomorin/xseries/ReflectionUtils*")
        exclude("com/cryptomorin/xseries/SkullCacheListener*")
        exclude("com/cryptomorin/xseries/SkullUtils*")
        exclude("com/cryptomorin/xseries/XBiome*")
        exclude("com/cryptomorin/xseries/XBlock*")
        exclude("com/cryptomorin/xseries/XEnchantment*")
        exclude("com/cryptomorin/xseries/XEntity*")
        exclude("com/cryptomorin/xseries/XItemStack*")
        exclude("com/cryptomorin/xseries/XPotion*")
        exclude("com/cryptomorin/xseries/XSound*")
        relocate("com.cryptomorin.xseries", "dev.jaqobb.rewardableactivities.library.com.cryptomorin.xseries")
        relocate("org.bstats.bukkit", "dev.jaqobb.rewardableactivities.metrics")
    }
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
    maven("https://repo.codemc.org/repository/maven-public/") {
        content {
            includeGroup("org.bstats")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    implementation("com.github.cryptomorin:XSeries:7.6.0.0.1")
    implementation("org.bstats:bstats-bukkit:1.7")
}
