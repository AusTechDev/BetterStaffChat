package dev.austech.betterstaffchat.spigot

import de.leonhard.storage.Json
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.DiscordManager
import dev.austech.betterstaffchat.common.libraries.BSCLibraries
import dev.austech.betterstaffchat.common.update.UpdateChecker
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.common.util.Data
import dev.austech.betterstaffchat.common.util.IPlayerUtil
import dev.austech.betterstaffchat.common.util.StaffChatUtil
import dev.austech.betterstaffchat.spigot.commands.CommandManager
import dev.austech.betterstaffchat.spigot.listeners.PlayerListener
import dev.austech.betterstaffchat.spigot.libby.BukkitLibraryManager
import dev.austech.betterstaffchat.spigot.util.PlayerUtil
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BSCSpigot(private val plugin: JavaPlugin) : BSCPlugin {
    companion object {
        lateinit var instance: BSCSpigot
    }

    override lateinit var staffChatUtil: StaffChatUtil
    override val playerUtil = PlayerUtil
    lateinit var audience: BukkitAudiences
    private lateinit var data: Json
    lateinit var dataWrapper: Data.Wrapper;
    override lateinit var config: Yaml
    override lateinit var consoleAudience: Audience;
    override lateinit var discordManager: DiscordManager
    override val platform = BSCPlugin.Platform.BUKKIT
    override val pluginDataFile: File = plugin.dataFolder
    lateinit var updateChecker: UpdateChecker

    override fun onLoad() {
        val libraryManager = BukkitLibraryManager(plugin)
        libraryManager.addMavenCentral()
        libraryManager.addJitPack()

        BSCLibraries.getLibraries(platform).forEach {
            libraryManager.loadLibrary(it)
        }
    }

    override fun onEnable() {
        instance = this;
        audience = BukkitAudiences.create(plugin)
        consoleAudience = audience.console()
        config = Config(this).load()
        data = Data(this).load()
        dataWrapper = Data.Wrapper(data)
        updateChecker = UpdateChecker(plugin)

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            discordManager = DiscordManager(this)
        })

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (updateChecker.needsUpdate) {
                plugin.logger.warning("An update for BetterStaffChat is available.")
                plugin.logger.warning("Latest version: ${ChatColor.GREEN}${updateChecker.latestVersion}${ChatColor.YELLOW}. Your version: ${ChatColor.RED}${updateChecker.currentVersion}")
                plugin.logger.warning("Update: ${ChatColor.BLUE}https://www.spigotmc.org/resources/91991/")
            }
        }, 1L)

        val shouldHookPAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") &&
                config.getBoolean(Config.Paths.Hooks.PLACEHOLDERAPI.toString())

        staffChatUtil = if (shouldHookPAPI) {
            StaffChatUtil(this) { uuid, s ->
                PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(uuid), s)
            }
        } else {
            StaffChatUtil(this)
        }

        CommandManager(this).registerCommands()

        Bukkit.getPluginManager().registerEvents(PlayerListener(), plugin)
    }
}