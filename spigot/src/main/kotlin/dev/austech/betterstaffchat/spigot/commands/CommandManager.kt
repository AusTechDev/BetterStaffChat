package dev.austech.betterstaffchat.spigot.commands

import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.BSCSpigot
import dev.austech.betterstaffchat.spigot.commands.impl.BetterStaffChatCommand
import dev.austech.betterstaffchat.spigot.commands.impl.StaffChatCommand
import dev.austech.betterstaffchat.spigot.commands.impl.StaffChatMuteCommand
import dev.austech.betterstaffchat.spigot.commands.impl.StaffChatToggleCommand
import dev.austech.betterstaffchat.spigot.util.ReflectionUtil
import org.bukkit.command.Command

class CommandManager(private val plugin: BSCSpigot) {
    private val commands: List<Command> = listOf(
        StaffChatCommand(
            plugin.config.getString(Config.Paths.COMMAND_STAFFCHAT_COMMAND.toString())?: "staffchat",
            "This command lets you send a message to the staffchat.",
            "<messages...>",
            plugin.config.getStringList(Config.Paths.COMMAND_STAFFCHAT_ALIASES.toString())
        ),
        StaffChatMuteCommand(
            plugin.config.getString(Config.Paths.COMMAND_MUTE_COMMAND.toString())?: "mutestaffchat",
            "This command lets you ignore / disable your staffchat.",
            "[on/off]",
            plugin.config.getStringList(Config.Paths.COMMAND_MUTE_ALIASES.toString())
        ),
        StaffChatToggleCommand(
            plugin.config.getString(Config.Paths.COMMAND_TOGGLE_COMMAND.toString())?: "togglestaffchat",
            "This command lets you ignore / disable your staffchat.",
            "[on/off]",
            plugin.config.getStringList(Config.Paths.COMMAND_TOGGLE_ALIASES.toString())
        ),
        BetterStaffChatCommand()
    )

    fun registerCommands() {
        commands.forEach {
            ReflectionUtil.registerCommand(it)
        }
    }
}
