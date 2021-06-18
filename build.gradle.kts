plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.jaqobb"
version = "1.8.4"
description = "Reward players when they do certain activities"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

bukkit {
    name = "RewardableActivities"
    main = "dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin"
    version = project.version as String
    apiVersion = "1.13"
    softDepend = listOf("Vault", "PlaceholderAPI", "MVdWPlaceholderAPI")
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
        relocate("com.cryptomorin.xseries", "dev.jaqobb.rewardableactivities.library.xseries")
        relocate("org.bstats", "dev.jaqobb.rewardableactivities.metrics")
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
    maven("http://nexus.hc.to/content/repositories/pub_releases/") {
        isAllowInsecureProtocol = true
        content {
            includeGroup("net.milkbowl.vault")
        }
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        content {
            includeGroup("me.clip")
        }
    }
    maven("http://repo.mvdw-software.be/content/groups/public/") {
        isAllowInsecureProtocol = true
        content {
            includeGroup("be.maximvdw")
        }
    }
    maven("https://repo.codemc.org/repository/maven-public/") {
        content {
            includeGroup("org.bstats")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("be.maximvdw:MVdWPlaceholderAPI:3.1.1-SNAPSHOT") {
        exclude("org.spigotmc")
    }
    implementation("com.github.cryptomorin:XSeries:8.1.0")
    implementation("org.bstats:bstats-bukkit:2.2.1")
}
