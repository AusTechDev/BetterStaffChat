package dev.austech.betterstaffchat.bungeecord

import de.leonhard.storage.Json
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.bungeecord.commands.CommandManager
import dev.austech.betterstaffchat.bungeecord.libby.BungeeLibraryManager
import dev.austech.betterstaffchat.bungeecord.listeners.PlayerListener
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.DiscordManager
import dev.austech.betterstaffchat.common.libraries.BSCLibraries
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.common.util.Data
import dev.austech.betterstaffchat.common.util.IPlayerUtil
import dev.austech.betterstaffchat.common.util.StaffChatUtil
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

class BSCBungee(private val plugin: Plugin): BSCPlugin {
    companion object {
        lateinit var instance: BSCBungee
    }

    override val platform: BSCPlugin.Platform = BSCPlugin.Platform.BUNGEECORD
    override val pluginDataFile: File = plugin.dataFolder
    override lateinit var config: Yaml
    override lateinit var consoleAudience: Audience
    override lateinit var staffChatUtil: StaffChatUtil
    override val playerUtil: IPlayerUtil = PlayerUtil
    override val discordManager: DiscordManager
        get() = TODO("Not yet implemented")
    lateinit var audience: BungeeAudiences
    private lateinit var data: Json
    lateinit var dataWrapper: Data.Wrapper;

    override fun onLoad() {
        val libraryManager = BungeeLibraryManager(plugin)
        libraryManager.addMavenCentral()
        libraryManager.addJitPack()

        BSCLibraries.getLibraries(platform).forEach {
            libraryManager.loadLibrary(it)
        }
    }

    override fun onEnable() {
        instance = this
        audience = BungeeAudiences.create(plugin)
        consoleAudience = audience.console()
        config = Config(this).load()
        staffChatUtil = StaffChatUtil(this)
        data = Data(this).load()
        dataWrapper = Data.Wrapper(data)

        CommandManager(this).registerCommands(plugin)

        ProxyServer.getInstance().pluginManager.registerListener(plugin, PlayerListener(plugin))
    }
}