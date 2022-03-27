package dev.austech.betterstaffchat.common

import de.leonhard.storage.Yaml
import net.kyori.adventure.audience.Audience
import java.io.File

interface BSCPlugin {
    val platform: Platform
    val pluginDataFile: File
    val config: Yaml
    val consoleAudience: Audience

    enum class Platform {
        BUKKIT,
        BUNGEECORD
    }
}