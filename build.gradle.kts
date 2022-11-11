plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.jaqobb"
version = "1.8.6-SNAPSHOT"
description = "Reward players when they do certain activities"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

bukkit {
    name = "RewardableActivities"
    main = "dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin"
    version = project.version as String
    apiVersion = "1.13"
    softDepend = listOf("Vault", "PlaceholderAPI")
    description = project.description
    author = "jaqobb"
    website = "https://jaqobb.dev"
    commands {
        create("rewardable-activities") {
            description = "Rewardable Activities main command"
            aliases = listOf("rewardableactivities")
        }
    }
}

tasks {
    shadowJar {
        exclude("com/cryptomorin/xseries/messages/*")
        exclude("com/cryptomorin/xseries/particles/*")
        exclude("com/cryptomorin/xseries/unused/*")
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
        exclude("com/cryptomorin/xseries/XTag*")
        relocate("com.cryptomorin.xseries", "dev.jaqobb.rewardable_activities.library.xseries")
        relocate("org.bstats", "dev.jaqobb.rewardable_activities.metrics")
    }
}

repositories {
    mavenCentral()
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
    maven("https://nexus.hc.to/content/repositories/pub_releases/") {
        content {
            includeGroup("net.milkbowl.vault")
        }
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        content {
            includeGroup("me.clip")
        }
    }
    maven("https://repo.codemc.org/repository/maven-public/") {
        content {
            includeGroup("org.bstats")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.2")
    implementation("com.github.cryptomorin:XSeries:9.1.0")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}
