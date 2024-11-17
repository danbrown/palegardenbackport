plugins {
    `maven-publish`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("dev.architectury.loom")
}

// Variables
class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name").toString()
    val version = property("mod.version").toString()
    val group = property("mod.group").toString()
    val authors = property("mod.authors").toString()
    val license = property("mod.license").toString()
    val description = property("mod.description").toString()
    val issue_tracker = property("mod.issue_tracker").toString()
    val discord = property("mod.discord").toString()
}

val mod = ModData()

val loader = loom.platform.get().name.lowercase()
val isFabric = loader == "fabric"
val isForge = loader == "forge"
val isNeoForge = loader == "neoforge"
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
val mcDep = property("mc.target").toString()
val isSnapshot = hasProperty("env.snapshot")

version = "${mod.version}+$mcVersion"
group = mod.group
base { archivesName.set("${mod.id}-$loader") }

// Dependencies
repositories {
    fun strictMaven(url: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    strictMaven("https://thedarkcolour.github.io/KotlinForForge/", "thedarkcolour")
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    fun modrinth(name: String, dep: Any?) = "maven.modrinth:$name:$dep"
    fun modules(vararg modules: String) {
        modules.forEach { modImplementation(fabricApi.module("fabric-$it", "${property("deps.fapi")}")) }
    }

    fun ifStable(str: String, action: (String) -> Unit = { modImplementation(it) }) {
        if (isSnapshot) modCompileOnly(str) else action(str)
    }

    minecraft("com.mojang:minecraft:${mcVersion}")
    @Suppress("UnstableApiUsage")
    mappings(loom.officialMojangMappings())
    if (isFabric) {
        modules("registry-sync-v0", "resource-loader-v0", "entity-events-v1", "screen-api-v1", "key-binding-api-v1", "lifecycle-events-v1")
        modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
        modImplementation("net.fabricmc:fabric-language-kotlin:${property("deps.flk")}+kotlin.2.0.0")
        ifStable("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    } else {
        if (loader == "forge") {
            "forge"("net.minecraftforge:forge:${mcVersion}-${property("fml.version")}")
            implementation("thedarkcolour:kotlinforforge:${property("deps.kff")}")
        } else{
            "neoForge"("net.neoforged:neoforge:${property("fml.version")}")
            implementation("thedarkcolour:kotlinforforge-neoforge:${property("deps.kff")}") {
                isTransitive = false
            }
//            compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
//            compileOnly("org.jetbrains.kotlin:kotlin-reflect")
//            compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core")
//            compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core")
//            compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json")
        }
    }
}

// Loom config
loom {
    if (loader == "forge") {
        forge {
            mixinConfigs(
                "${mod.id}.mixins.json",
            )
        }
    }
    else if (loader == "neoforge") {
        neoForge {
            runs {
                
            }
        }
    }

    runConfigs.all {
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
        if (environment == "client") programArgs("--username=DanBrown_")
    }

    decompilers {
        get("vineflower").apply {
            options.put("mark-corresponding-synthetics", "1")
        }
    }
}


afterEvaluate {
    stonecutter {
        val platform = loom.platform.get().name.lowercase()
        stonecutter.const("fabric", platform == "fabric")
        stonecutter.const("forge", platform == "forge")
        stonecutter.const("neoforge", platform == "neoforge")
    }
}

// Tasks
if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

// Resources
tasks.processResources {
    inputs.property("version", mod.version)
    inputs.property("mc", mcDep)

    val map = mapOf(
        "mod_id" to mod.id,
        "mod_name" to mod.name,
        "mod_group" to mod.group,
        "mod_version" to mod.version,
        "mod_authors" to mod.authors,
        "mod_license" to mod.license,
        "mod_description" to mod.description,
        "mod_issue_tracker" to mod.issue_tracker,
        "mod_discord" to mod.discord,
        "target_minecraft" to mcDep,
        "target_loader" to stonecutter.project.property("deps.target_loader").toString(),
        "target_forge" to stonecutter.project.property("deps.target_forge").toString(),
//        "fml" to if (loader == "neoforge") "1" else "45",
//        "mnd" to if (loader == "neoforge") "" else "mandatory = true"
    )

    if(isFabric) {
        filesMatching("fabric.mod.json") { expand(map) }
        exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml")
    }
    else if (isForge){
        filesMatching("META-INF/mods.toml") { expand(map) }
        exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
    }
    else if (isNeoForge) {
        filesMatching("META-INF/neoforge.mods.toml") { expand(map) }
        exclude("fabric.mod.json", "META-INF/mods.toml")
    }

}

// Env configuration
stonecutter {
    val shouldUse21 = eval(mcVersion, ">=1.20.6")
    java {
        withSourcesJar()
        sourceCompatibility = if (shouldUse21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
        targetCompatibility = if (shouldUse21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(if (shouldUse21) 21 else 17)
    }
}
