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

    private val JDA = Library.builder()
        .groupId("net{}dv8tion")
        .artifactId("JDA")
        .version("5.0.0-alpha.9")
        .relocate("net{}dv8tion{}jda", "$relocatePath{}discord")
        .relocate("com{}neovisionaries{}nv-websocket-client", "$relocatePath{}nv-websocket-client")
        .relocate("okhttp", "$relocatePath{}okhttp")
        .relocate("org{}slf4j", "$relocatePath{}slf4j")
        .relocate("org{}apache{}commons{}commons-collections4", "$relocatePath{}commons-collections4")
        .relocate("com{}squareup{}okio", "$relocatePath{}okio")
        .relocate("net{}sf{}trove4j", "$relocatePath{}trove4j")
        .relocate("com{}fasterxml{}jackson{}databind", "$relocatePath{}jackson-databind")
        .relocate("com{}fasterxml{}jackson{}annotations", "$relocatePath{}jackson-annotations")
        .relocate("com{}fasterxml{}jackson{}core", "$relocatePath{}jackson-core")
        .build()

    private val OKHTTP = Library.builder()
        .groupId("com{}squareup{}okhttp3")
        .artifactId("okhttp")
        .version("4.9.3")
        .relocate("okhttp", "$relocatePath{}okhttp")
        .relocate("_okio".replace("_", ""), "$relocatePath{}okio")
        .relocate("_kotlin".replace("_", ""), "$relocatePath{}kotlin")
        .build()

    private val NV_WEBSOCKET_CLIENT = Library.builder()
        .groupId("com{}neovisionaries")
        .artifactId("nv-websocket-client")
        .version("2.14")
        .relocate("com{}neovisionaries{}nv-websocket-client", "$relocatePath{}nv-websocket-client")
        .build()

    private val SLF4J = Library.builder()
        .groupId("org{}slf4j")
        .artifactId("slf4j-api")
        .version("1.7.25")
        .relocate("org{}slf4j", "$relocatePath{}slf4j")
        .build()

    private val COMMONS_COLLECTIONS4 = Library.builder()
        .groupId("org{}apache{}commons")
        .artifactId("commons-collections4")
        .version("4.1")
        .relocate("org{}apache{}commons{}commons-collections4", "$relocatePath{}commons-collections4")
        .build()

    private val TROVE4J = Library.builder()
        .groupId("net{}sf{}trove4j")
        .artifactId("trove4j")
        .version("3.0.3")
        .relocate("net{}sf{}trove4j", "$relocatePath{}trove4j")
        .build()

    private val JACKSON_DATABIND = Library.builder()
        .groupId("com{}fasterxml{}jackson{}core")
        .artifactId("jackson-databind")
        .version("2.10.1")
        .relocate("com{}fasterxml{}jackson{}databind", "$relocatePath{}jackson-databind")
        .relocate("com{}fasterxml{}jackson{}annotations", "$relocatePath{}jackson-annotations")
        .relocate("com{}fasterxml{}jackson{}core", "$relocatePath{}jackson-core")
        .build()

    private val JACKSON_ANNOTATIONS = Library.builder()
        .groupId("com{}fasterxml{}jackson{}core")
        .artifactId("jackson-annotations")
        .version("2.10.1")
        .relocate("com{}fasterxml{}jackson{}databind", "$relocatePath{}jackson-databind")
        .relocate("com{}fasterxml{}jackson{}annotations", "$relocatePath{}jackson-annotations")
        .relocate("com{}fasterxml{}jackson{}core", "$relocatePath{}jackson-core")
        .build()

    private val JACKSON_CORE = Library.builder()
        .groupId("com{}fasterxml{}jackson{}core")
        .artifactId("jackson-core")
        .version("2.10.1")
        .relocate("com{}fasterxml{}jackson{}databind", "$relocatePath{}jackson-databind")
        .relocate("com{}fasterxml{}jackson{}annotations", "$relocatePath{}jackson-annotations")
        .relocate("com{}fasterxml{}jackson{}core", "$relocatePath{}jackson-core")
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
            SIMPLIX_STORAGE,
            JDA,
            OKHTTP,
            NV_WEBSOCKET_CLIENT,
            SLF4J,
            COMMONS_COLLECTIONS4,
            TROVE4J,
            JACKSON_DATABIND,
            JACKSON_ANNOTATIONS,
            JACKSON_CORE
        )

        if (platform == BSCPlugin.Platform.BUKKIT) {
            libraryList = libraryList.plus(Adventure.PLATFORM_BUKKIT)
        } else if (platform == BSCPlugin.Platform.BUNGEECORD) {
            libraryList = libraryList.plus(Adventure.PLATFORM_BUNGEECORD)
                .plus(Adventure.SERIALIZER_BUNGEECORD)
        }

        return libraryList
    }
}