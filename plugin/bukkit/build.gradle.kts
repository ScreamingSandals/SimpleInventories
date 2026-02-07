import org.screamingsandals.gradle.builder.*

plugins {
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.screaming.plugin.slib)
}

dependencies {
    implementation(project(":core-bukkit"))
    implementation(libs.configurate.gson)
    implementation(libs.configurate.hocon)
    implementation(libs.configurate.xml)
    implementation(libs.configurate.yaml)
    compileOnly(libs.paper)
}

configureShadowPlugin {
    relocate("org.screamingsandals.lib", "org.screamingsandals.simpleinventories.lib")
    relocate("cloud.commandframework", "org.screamingsandals.simpleinventories.lib.ext.cloud")
    relocate("io.leangen.geantyref", "org.screamingsandals.simpleinventories.lib.ext.geantyref")
    relocate("org.spongepowered.configurate", "org.screamingsandals.simpleinventories.lib.ext.configurate")
    relocate("org.yaml.snakeyaml", "org.screamingsandals.simpleinventories.lib.ext.snakeyaml")
    relocate("com.typesafe.config", "org.screamingsandals.simpleinventories.lib.ext.typesafe")
}

buildConfig {
    className("VersionInfo")
    packageName("org.screamingsandals.simpleinventories")

    buildConfigField("String", "NAME", "\"${project.name}\"")
    buildConfigField("String", "VERSION", "\"${project.version}\"")
}

slib {
    version(rootProject.libs.versions.screaming.lib)
    platforms("bukkit")

    additionalContent {
        module("cloud")
    }
}