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
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    
    dependencies {
        "compileOnly"(rootProject.libs.jetbrains.annotations)
    	"compileOnly"(rootProject.libs.paper)
    }

    configureLicenser()
    configureJavac(JavaVersion.VERSION_1_8)

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:-options")
    }

    val buildSources = name.startsWith("SimpleInventories-Core")
    if (buildSources) {
        configureSourcesJar()
    }

    setupMavenPublishing(addSourceJar=buildSources) {
        pom {
            name.set("SimpleInventories")
            description.set("Simple tree gui generator for Bukkit plugins!")
            url.set("https://github.com/ScreamingSandals/SimpleInventories")
            licenses {
                license {
                    name.set("Apache License 2.0")
                    url.set("https://github.com/ScreamingSandals/ScreamingLib/blob/ver/1.0.x/LICENSE")
                }
            }
        }
    }

    setupMavenRepositoriesFromProperties()
}
