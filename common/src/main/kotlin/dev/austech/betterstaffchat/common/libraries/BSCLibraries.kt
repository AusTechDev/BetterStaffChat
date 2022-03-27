package dev.austech.betterstaffchat.common.libraries

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.libraries.libby.Library

object BSCLibraries {
    private const val relocatePath = "dev{}austech{}betterstaffchat{}depend"

    private object Adventure {
        private val adventureBuilder = Library.builder()
            .groupId("net{}kyori")
            .relocate("net{}kyori{}adventure", "$relocatePath{}adventure")
            .relocate("net{}kyori{}examination", "$relocatePath{}adventure{}util{}examination")

        val API: Library = adventureBuilder
            .artifactId("adventure-api")
            .version("4.10.1")
            .build()

        val NBT: Library = adventureBuilder
            .artifactId("adventure-nbt")
            .version("4.10.1")
            .build()

        val KEY: Library = adventureBuilder
            .artifactId("adventure-key")
            .version("4.10.1")
            .build()

        val MINIMESSAGE: Library = adventureBuilder
            .artifactId("adventure-text-minimessage")
            .version("4.10.1")
            .build()

        val PLATFORM: Library = adventureBuilder
            .artifactId("adventure-platform-api")
            .version("4.1.0")
            .build()

        val PLATFORM_FACET: Library = adventureBuilder
            .artifactId("adventure-platform-facet")
            .version("4.1.0")
            .build()

        val PLATFORM_BUKKIT: Library = adventureBuilder
            .artifactId("adventure-platform-bukkit")
            .version("4.1.0")
            .build()

        val PLATFORM_BUNGEECORD: Library = adventureBuilder
            .artifactId("adventure-platform-bungee")
            .version("4.1.0")
            .build()

        val SERIALIZER_BUNGEECORD: Library = adventureBuilder
            .artifactId("adventure-text-serializer-bungeecord")
            .version("4.1.0")
            .build()

        val SERIALIZER_LEGACY: Library = adventureBuilder
            .artifactId("adventure-text-serializer-legacy")
            .version("4.10.1")
            .build()

        val SERIALIZER_GSON: Library = adventureBuilder
            .artifactId("adventure-text-serializer-gson")
            .version("4.10.1")
            .build()

        val SERIALIZER_GSON_LEGACY_IMPL: Library = adventureBuilder
            .artifactId("adventure-text-serializer-gson-legacy-impl")
            .version("4.10.1")
            .build()

        val EXAMINATION: Library = adventureBuilder
            .artifactId("examination-api")
            .version("1.3.0")
            .build()

        val EXAMINATION_STRING: Library = adventureBuilder
            .artifactId("examination-string")
            .version("1.3.0")
            .build()
    }

    private val SIMPLIX_STORAGE = Library.builder()
        .groupId("com{}github{}simplix-softworks")
        .artifactId("SimplixStorage")
        .version("3.2.3")
        .relocate("de{}leonhard{}storage", "$relocatePath{}storage")
        .build()

    fun getLibraries(platform: BSCPlugin.Platform?): List<Library> {
        var libraryList: List<Library> = listOf(
            Adventure.API,
            Adventure.NBT,
            Adventure.KEY,
            Adventure.MINIMESSAGE,
            Adventure.SERIALIZER_LEGACY,
            Adventure.SERIALIZER_GSON,
            Adventure.SERIALIZER_GSON_LEGACY_IMPL,
            Adventure.PLATFORM,
            Adventure.PLATFORM_FACET,
            Adventure.EXAMINATION,
            Adventure.EXAMINATION_STRING,
            SIMPLIX_STORAGE
        )

        if (platform == BSCPlugin.Platform.BUKKIT) {
            libraryList = libraryList.plus(Adventure.PLATFORM_BUKKIT)
        } else if (platform == BSCPlugin.Platform.BUNGEECORD) {
            libraryList = libraryList.plus(Adventure.PLATFORM_BUNGEECORD)
                .plus(Adventure.SERIALIZER_BUNGEECORD)
        }

        println("Libraries: ${libraryList.joinToString { it.artifactId.toString() }}")

        return libraryList
    }
}