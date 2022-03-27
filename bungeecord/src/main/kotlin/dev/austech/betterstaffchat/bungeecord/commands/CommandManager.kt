package dev.austech.betterstaffchat.bungeecord.commands

import dev.austech.betterstaffchat.bungeecord.BSCBungee
import dev.austech.betterstaffchat.bungeecord.commands.impl.BetterStaffChatCommand
import dev.austech.betterstaffchat.bungeecord.commands.impl.StaffChatCommand
import dev.austech.betterstaffchat.bungeecord.commands.impl.StaffChatMuteCommand
import dev.austech.betterstaffchat.bungeecord.commands.impl.StaffChatToggleCommand
import dev.austech.betterstaffchat.common.util.Config
import net.md_5.bungee.api.plugin.Command

class CommandManager(private val plugin: BSCBungee) {
    private val commands: List<Command> = listOf(
        StaffChatCommand(
            plugin.config.getString(Config.Paths.COMMAND_STAFFCHAT_COMMAND.toString())?: "staffchat",
            plugin.config.getStringList(Config.Paths.COMMAND_STAFFCHAT_ALIASES.toString())
        ),
        StaffChatMuteCommand(
            plugin.config.getString(Config.Paths.COMMAND_MUTE_COMMAND.toString())?: "mutestaffchat",
            plugin.config.getStringList(Config.Paths.COMMAND_MUTE_ALIASES.toString())
        ),
        StaffChatToggleCommand(
            plugin.config.getString(Config.Paths.COMMAND_TOGGLE_COMMAND.toString())?: "togglestaffchat",
            plugin.config.getStringList(Config.Paths.COMMAND_TOGGLE_ALIASES.toString())
        ),
        BetterStaffChatCommand()
    )

    fun registerCommands() {
        commands.forEach {
            plugin.proxy.pluginManager.registerCommand(plugin, it)
        }
    }
}