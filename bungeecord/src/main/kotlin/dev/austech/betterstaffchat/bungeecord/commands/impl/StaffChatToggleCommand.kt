package dev.austech.betterstaffchat.bungeecord.commands.impl

import dev.austech.betterstaffchat.bungeecord.commands.BSCBungeeCommand
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.util.Config
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer

class StaffChatToggleCommand(name: String, aliases: List<String>): BSCBungeeCommand(name, "betterstaffchat.mutestaffchat", aliases) {
    override fun run(sender: CommandSender, args: Array<out String>) {
        if (!plugin.config.getBoolean(Config.Paths.Staffchat.TOGGLE_ENABLED.toString())) {
            errorTell("Staffchat toggle is not enabled.")
            return
        }

        if (sender.requirePlayer()) {
            return
        }

        if (sender !is ProxiedPlayer) {
            throw IllegalStateException("Sender is not a player!")
        }

        if (PlayerUtil.isDisabledServer(sender.server.info)) {
            errorTell("You cannot use staffchat on this server.")
            return;
        }

        val toggleOn: Boolean
        val toggledPlayers = plugin.dataWrapper.getToggledPlayers()

        if (args.isNotEmpty()) {
            if (args[0].equals("off", true)) {
                if (!toggledPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is not toggled.")
                    return
                }

                toggleOn = false
            } else if (args[0].equals("on", true)) {
                if (toggledPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is already toggled.")
                    return
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
                return
            }

            plugin.dataWrapper.addToggledPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.Staffchat.TOGGLE_MESSAGE_ON.toString()))
        } else {
            plugin.dataWrapper.removeToggledPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.Staffchat.TOGGLE_MESSAGE_OFF.toString()))
        }

        return
    }
}