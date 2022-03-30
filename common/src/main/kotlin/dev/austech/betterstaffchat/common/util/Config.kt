package dev.austech.betterstaffchat.common.util

import de.leonhard.storage.LightningBuilder
import de.leonhard.storage.Yaml
import dev.austech.betterstaffchat.common.BSCPlugin
import net.kyori.adventure.text.Component
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
            .createConfig()
    }

    class Paths {
        enum class Staffchat(private val path: String) {
            FORMAT("staffchat.format"),
            PREFIX("staffchat.prefix"),
            CONSOLE_NAME("staffchat.console-replacement"),
            CONSOLE_UUID("staffchat.console-uuid-replacement"),
            CONSOLE_SERVER("staffchat.console-server-replacement"),
            CONSOLE_LOG("staffchat.log-to-console"),
            STRIP_COLOR_CODES("staffchat.strip-color-codes"),
            SERVER_REPLACEMENTS("staffchat.server-replacement"),
            DISABLED_SERVERS("staffchat.disabled-servers"),

            EVENTS_JOIN_ENABLED("staffchat.events.join.enabled"),
            EVENTS_JOIN_SEE_OWN("staffchat.events.join.see-own-join"),
            EVENTS_JOIN_MESSAGE("staffchat.events.join.message"),
            EVENTS_LEAVE_ENABLED("staffchat.events.leave.enabled"),
            EVENTS_LEAVE_MESSAGE("staffchat.events.leave.message"),
            EVENTS_SWITCH_ENABLED("staffchat.events.switch.enabled"),
            EVENTS_SWITCH_USE_WORLDS("staffchat.events.switch.use-worlds-on-spigot"),
            EVENTS_SWITCH_MESSAGE("staffchat.events.switch.message"),

            MUTE_ENABLED("staffchat.mute.enabled"),
            MUTE_MESSAGE_ON("staffchat.mute.on"),
            MUTE_MESSAGE_OFF("staffchat.mute.off"),
            TOGGLE_ENABLED("staffchat.toggle.enabled"),
            TOGGLE_MESSAGE_ON("staffchat.toggle.on"),
            TOGGLE_MESSAGE_OFF("staffchat.toggle.off");

            override fun toString(): String {
                return path
            }
        }

        enum class Command(private val path: String) {
            STAFFCHAT_COMMAND("commands.staffchat.command"),
            STAFFCHAT_ALIASES("commands.staffchat.aliases"),
            MUTE_COMMAND("commands.mutestaffchat.command"),
            MUTE_ALIASES("commands.mutestaffchat.aliases"),
            TOGGLE_COMMAND("commands.togglestaffchat.command"),
            TOGGLE_ALIASES("commands.togglestaffchat.aliases");

            override fun toString(): String {
                return path
            }
        }

        enum class Hooks(private val path: String) {
            LUCKPERMS("hooks.luckperms"),
            PLACEHOLDERAPI("hooks.placeholderapi");

            override fun toString(): String {
                return path
            }
        }

        enum class Discord(private val path: String) {
            ENABLED("discord.enabled"),
            TYPE("discord.type"),

            BOT_TOKEN("discord.bot.token"),
            BOT_STATUS("discord.bot.status"),
            BOT_ACTIVITY("discord.bot.activity.enabled"),
            BOT_ACTIVITY_TYPE("discord.bot.activity.type"),
            BOT_ACTIVITY_STATUS("discord.bot.activity.status"),
            BOT_CHANNELS("discord.bot.channels"),

            WEBHOOK_URL("discord.webhook.url"),

            ALLOWED_MENTIONS("discord.allowed-mentions"),

            NAME_FORMAT("discord.messages.name-format");

            override fun toString(): String {
                return path
            }

            class Message(event: String) {
                val mode = "discord.messages.$event.mode"
                val embed = "discord.messages.$event.embed"
                val text = "discord.messages.$event.text"
            }
        }
    }
}