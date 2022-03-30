package dev.austech.betterstaffchat.common

import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.common.discord.DiscordManager
import dev.austech.betterstaffchat.common.util.IPlayerUtil
import dev.austech.betterstaffchat.common.util.StaffChatUtil
import net.kyori.adventure.audience.Audience
import java.io.File

interface BSCPlugin {
    val platform: Platform
    val pluginDataFile: File
    val config: Yaml
    val consoleAudience: Audience
    val staffChatUtil: StaffChatUtil
    val playerUtil: IPlayerUtil
    val discordManager: DiscordManager

    fun onLoad()
    fun onEnable()

    enum class Platform {
        BUKKIT,
        BUNGEECORD
    }
}