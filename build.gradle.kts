import me.modmuss50.mpp.ReleaseType

plugins {
	id("fabric-loom") version "1.10-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "2.2.1"
}

version = property("mod_version") as String
group = property("maven_group") as String

var modId: String = property("mod_id").toString()
base {
    archivesName.set(modId)
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
}

tasks.processResources {
    val map = mapOf(
        "version" to version,
        "minecraft" to project.property("minecraft_version"),
        "loader" to project.property("loader_version"),
    )

    inputs.properties(map)

	filesMatching("fabric.mod.json") {
        expand(map)
	}
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release = 17
}

java {
    withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${modId}"}
	}
}

@Override
fun file(path: String): File {
    return rootProject.file(path)
}

@Override
fun fileTree(path: String): ConfigurableFileTree {
    return rootProject.fileTree(path)
}

val changelogText: String = if (file("CHANGELOG.md").exists()) {
    file("CHANGELOG.md").readText()
} else {
    "No changelog provided"
}

val projectVersion: String = property("mod_version").toString()
val projectVersionNumber: List<String> = projectVersion.split(Regex("-"), 2)
var projectVersionName = "Release ${projectVersionNumber[0]}"
var projectVersionType = ReleaseType.STABLE
if (projectVersion.contains("beta")) {
    val projectBeta: List<String> = projectVersionNumber[1].split(Regex("\\."), 2)
    projectVersionName = "${projectVersionNumber[0]} - Beta ${projectBeta[1]}"
    projectVersionType = ReleaseType.BETA
} else if (projectVersion.contains("alpha")) {
    val projectAlpha: List<String> = projectVersionNumber[1].split(Regex("\\."), 2)
    projectVersionName = "${projectVersionNumber[0]} - Alpha ${projectAlpha[1]}"
    projectVersionType = ReleaseType.ALPHA
} else if (projectVersion.contains("rc")) {
    val projectRC: List<String> = projectVersionNumber[1].split(Regex("\\."), 2)
    projectVersionName = "${projectVersionNumber[0]} - Release Candidate ${projectRC[1]}"
    projectVersionType = ReleaseType.BETA
}

val versions = listOf(
    "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
    "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11",
    "26.1", "26.1.1", "26.1.2",
    "26.2",
    "26.3",
)

publishMods {
    file = tasks.jar.get().archiveFile
    changelog = changelogText
    type = projectVersionType
    modLoaders.add("fabric")
    displayName = projectVersionName

    modrinth {
        projectId = "f63ZlDVt"
        accessToken = providers.gradleProperty("MODRINTH_TOKEN")
        minecraftVersions = versions

        changelog = changelogText
    }

    curseforge {
        projectId = "1751247"
        projectSlug = "better-narrator-error" // Required for discord webhook
        accessToken = providers.gradleProperty("CURSEFORGE_TOKEN")
        minecraftVersions = versions
        client = true

        changelog = changelogText
    }

    /*discord {
        webhookUrl = providers.gradleProperty("DISCORD_WEBHOOK")
        dryRunWebhookUrl = providers.gradleProperty("DRY_WEBHOOK")

        username = "Mod updates"
        avatarUrl = "https://cdn.discordapp.com/avatars/1268055578073108574/73106a33f497ea5f2c676bcfb4816917.webp"

        content = """
                # Better Narrator Error | $projectVersionName

                ${if (changelogText.length > 4000) "Changelog is too long, check it on [Modrinth](https://modrinth.com/mod/better-narrator-error/changelog)" else changelogText}
            """.trimIndent()

        style {
            look = "MODERN"
            link = "BUTTON"
            thumbnailUrl = "https://cdn.modrinth.com/data/BMaqFQAd/116458c672aadeb31856563eaff8ed7edd764753.png"
            color = "modrinth"
        }
    }*/
}
