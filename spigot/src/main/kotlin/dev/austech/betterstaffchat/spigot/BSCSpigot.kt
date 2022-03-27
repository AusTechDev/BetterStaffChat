package dev.austech.betterstaffchat.spigot

import de.leonhard.storage.Json
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.libraries.BSCLibraries
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.common.util.Data
import dev.austech.betterstaffchat.common.util.StaffChatUtil
import dev.austech.betterstaffchat.spigot.commands.CommandManager
import dev.austech.betterstaffchat.spigot.listeners.PlayerListener
import dev.austech.betterstaffchat.spigot.libby.BukkitLibraryManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BSCSpigot: JavaPlugin(), BSCPlugin {
    companion object {
        lateinit var instance: BSCSpigot
    }

    lateinit var staffChatUtil: StaffChatUtil
    lateinit var audience: BukkitAudiences
    private lateinit var data: Json
    lateinit var dataWrapper: Data.Wrapper;
    override lateinit var config: Yaml
    override lateinit var consoleAudience: Audience;
    override val platform = BSCPlugin.Platform.BUKKIT
    override val pluginDataFile: File = this.dataFolder

    override fun onLoad() {
        val libraryManager = BukkitLibraryManager(this)
        libraryManager.addMavenCentral()
        libraryManager.addJitPack()

        BSCLibraries.getLibraries(platform).forEach {
            libraryManager.loadLibrary(it)
        }
    }

    override fun onEnable() {
        instance = this;
        audience = BukkitAudiences.create(this)
        consoleAudience = audience.console()
        config = Config(this).load()
        staffChatUtil = StaffChatUtil(this)
        data = Data(this).load()
        dataWrapper = Data.Wrapper(data)

        CommandManager(this).registerCommands()

        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)
    }
}
