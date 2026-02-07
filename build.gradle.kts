import io.freefair.gradle.plugins.lombok.LombokPlugin
import org.screamingsandals.gradle.builder.*

plugins {
    alias(libs.plugins.screaming.plugin.builder) apply(false)
    alias(libs.plugins.lombok) apply(false)
}

defaultTasks("clean", "build")

subprojects {
    apply<JavaPlugin>()
    apply<BuilderPlugin>()
    apply<LombokPlugin>()

    repositories {
        mavenCentral()
        maven("https://repo.screamingsandals.org/public/")
        maven("https://repo.papermc.io/repository/maven-snapshots/")
    }

    dependencies {
        "compileOnly"(rootProject.libs.jetbrains.annotations)
    }

    configureLicenser()
    configureJavac(JavaVersion.VERSION_11)

    val buildSources = name.startsWith("core-")
    if (buildSources) {
        configureSourcesJar()
    }

    setupMavenRepositoriesFromProperties()

    // TODO: check if this is needed (and probably remove it later)
    configurations.all {
        // Check for updates every build
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }

    afterEvaluate { // because of shadow configured in plugin-bukkit
        setupMavenPublishing(
            addSourceJar=buildSources,
            addJavadocJar=(name == "core-common" && !version.toString().endsWith("-SNAPSHOT") || System.getenv("FORCE_JAVADOC") == "true"),
        ) {
            pom {
                name.set("SimpleInventories")
                description.set("Simple tree gui generator for ScreamingLib plugins!")
                url.set("https://github.com/ScreamingSandals/SimpleInventories")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://github.com/ScreamingSandals/ScreamingLib/blob/ver/2.0.x/LICENSE")
                    }
                }
            }
        }
    }
}
