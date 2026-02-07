# SimpleInventories
[![Simple Inventories CI](https://github.com/ScreamingSandals/SimpleInventories/actions/workflows/publish.yml/badge.svg)](https://github.com/ScreamingSandals/SimpleInventories/actions/workflows/gradle.yml)

**If you are using version 1.0.x (part of ScreamingBedWars 0.2.x), you are probably looking for the [ver/1.0.x](https://github.com/ScreamingSandals/BedWars/tree/ver/1.0.x) branch instead. This version of readme does NOT apply to 1.0.x!**

This README is WIP. Look into our wiki: https://github.com/ScreamingSandals/SimpleInventories/wiki (most format stuff is similar)

## Compiling

This project uses **Gradle** and requires **JDK 17** or newer (the compiled JARs require JDK 11 or newer to run). To build it, clone the repository and run:

```bash
./gradlew clean build
```

On Windows, use:

```bat
gradlew.bat clean build
```

The compiled JAR file for each module will be located in the `build/libs` folder of each subproject. You can also publish it to your local maven repository (`gradlew publishToMavenLocal`).

The compiled plugin JAR file will be located in the `plugin/{platform}/build/libs` directory (e.g. `plugin/bukkit/build/libs`).

## License

This project is licensed under the **Apache License 2.0** License - see the [LICENSE](LICENSE) file for details.
