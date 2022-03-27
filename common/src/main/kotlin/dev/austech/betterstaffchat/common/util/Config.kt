package dev.austech.betterstaffchat.common.util

import de.leonhard.storage.LightningBuilder
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.common.BSCPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.io.File
import java.nio.file.Files

class Config(private val plugin: BSCPlugin) {
    private val configFile: File = File(plugin.pluginDataFile, "config.yml")

    fun load(): Yaml {
        plugin.pluginDataFile.mkdirs()

        if (!configFile.exists()) {
            plugin.javaClass.getResourceAsStream((if (plugin.platform == BSCPlugin.Platform.BUKKIT) "/" else "") + "config.yml")
                .use {
                    Files.copy(it, configFile.toPath())
                }
        }



        return LightningBuilder.fromFile(configFile)
            .reloadCallback {
                plugin.consoleAudience.sendMessage(
                    Component.text("[BetterStaffChat] Reload requested or config.yml change. Reloading.")
                )
            }
            .createConfig();
    }

    enum class Paths(private val path: String) {
        STAFFCHAT_FORMAT("staffchat.format"),
        STAFFCHAT_PREFIX("staffchat.prefix"),
        STAFFCHAT_CONSOLE("staffchat.console-replacement"),
        STAFFCHAT_CONSOLE_UUID("staffchat.console-uuid-replacement"),
        STAFFCHAT_CONSOLE_SERVER("staffchat.console-server-replacement"),
        STAFFCHAT_CONSOLE_LOG("staffchat.log-to-console"),
        STAFFCHAT_STRIP_COLOR_CODES("staffchat.strip-color-codes"),
        STAFFCHAT_SERVER_REPLACEMENTS("staffchat.server-replacement"),
        STAFFCHAT_JOIN_MESSAGE("staffchat.join"),
        STAFFCHAT_LEAVE_MESSAGE("staffchat.leave"),
        STAFFCHAT_SWITCH_MESSAGE("staffchat.switch"),
        STAFFCHAT_MUTE_ON_MESSAGE("staffchat.mute-on"),
        STAFFCHAT_MUTE_OFF_MESSAGE("staffchat.mute-off"),
        STAFFCHAT_TOGGLE_ON_MESSAGE("staffchat.toggle-on"),
        STAFFCHAT_TOGGLE_OFF_MESSAGE("staffchat.toggle-off"),

        COMMAND_STAFFCHAT_COMMAND("commands.staffchat.command"),
        COMMAND_STAFFCHAT_ALIASES("commands.staffchat.aliases"),
        COMMAND_MUTE_COMMAND("commands.mutestaffchat.command"),
        COMMAND_MUTE_ALIASES("commands.mutestaffchat.aliases"),
        COMMAND_TOGGLE_COMMAND("commands.togglestaffchat.command"),
        COMMAND_TOGGLE_ALIASES("commands.togglestaffchat.aliases"),

        GLOBAL_UPDATE_CHECK("check-for-updates"),
        GLOBAL_DEBUG_MODE("debug");

        override fun toString(): String {
            return path;
        }
    }
}