package dev.austech.betterstaffchat.spigot.commands.impl

import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.commands.BSCSpigotCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffChatToggleCommand(name: String, description: String, usage: String, aliases: List<String>) : BSCSpigotCommand(name, description, usage, aliases) {
    override fun run(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.requirePermission("betterstaffchat.togglestaffchat")) {
            return true
        }

        if (!sender.requirePlayer()) {
            return true
        }

        if (sender !is Player) {
            throw IllegalStateException("Sender is not a player!")
        }

        val toggleOn: Boolean
        val toggledPlayers = plugin.dataWrapper.getToggledPlayers()

        if (args.isNotEmpty()) {
            if (args[0].equals("off", true)) {
                if (!toggledPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is not toggled.")
                    return true
                }

                toggleOn = false
            } else if (args[0].equals("on", true)) {
                if (toggledPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is already toggled.")
                    return true
                }

                toggleOn = true
            } else {
                toggleOn = !toggledPlayers.contains(sender.uniqueId)
            }
        } else {
            toggleOn = !toggledPlayers.contains(sender.uniqueId)
        }

        if (toggleOn) {
            val mutedPlayers = plugin.dataWrapper.getMutedPlayers()

            if (mutedPlayers.contains(sender.uniqueId)) {
                errorTell("Your staffchat is muted. Please unmute it before toggling.")
                return true
            }

            plugin.dataWrapper.addToggledPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.STAFFCHAT_TOGGLE_ON_MESSAGE.toString()))
        } else {
            plugin.dataWrapper.removeToggledPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.STAFFCHAT_TOGGLE_OFF_MESSAGE.toString()))
        }

        return true
    }
}