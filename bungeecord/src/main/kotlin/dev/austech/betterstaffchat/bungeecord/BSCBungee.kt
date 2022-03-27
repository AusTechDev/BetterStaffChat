package dev.austech.betterstaffchat.bungeecord

import de.leonhard.storage.Json
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.bungeecord.commands.CommandManager
import dev.austech.betterstaffchat.bungeecord.libby.BungeeLibraryManager
import dev.austech.betterstaffchat.bungeecord.listeners.PlayerListener
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.libraries.BSCLibraries
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.common.util.Data
import dev.austech.betterstaffchat.common.util.StaffChatUtil
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

class BSCBungee: Plugin(), BSCPlugin {
    companion object {
        lateinit var instance: BSCBungee
    }

    override val platform: BSCPlugin.Platform = BSCPlugin.Platform.BUNGEECORD
    override val pluginDataFile: File = dataFolder
    override lateinit var config: Yaml
    override lateinit var consoleAudience: Audience
    lateinit var staffChatUtil: StaffChatUtil
    lateinit var audience: BungeeAudiences
    private lateinit var data: Json
    lateinit var dataWrapper: Data.Wrapper;

    override fun onLoad() {
        val libraryManager = BungeeLibraryManager(this)
        libraryManager.addMavenCentral()
        libraryManager.addJitPack()

        BSCLibraries.getLibraries(platform).forEach {
            libraryManager.loadLibrary(it)
        }
    }

    override fun onEnable() {
        instance = this
        audience = BungeeAudiences.create(this)
        consoleAudience = audience.console()
        config = Config(this).load()
        staffChatUtil = StaffChatUtil(this)
        data = Data(this).load()
        dataWrapper = Data.Wrapper(data)

        CommandManager(this).registerCommands()

        proxy.pluginManager.registerListener(this, PlayerListener())
    }
}
