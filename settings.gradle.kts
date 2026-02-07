pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.screamingsandals.org/public/")
    }
}

rootProject.name = "SimpleInventories-parent"

setupProject("core-common", "core/common")
setupProject("core-bukkit", "core/bukkit")
setupProject("plugin-bukkit", "plugin/bukkit")

fun setupProject(name: String, folder: String, mkdir: Boolean = false) {
    include(name)
    project(":$name").let {
        it.projectDir = file(folder)
        if (mkdir) {
            it.projectDir.mkdirs()
        }
    }
}