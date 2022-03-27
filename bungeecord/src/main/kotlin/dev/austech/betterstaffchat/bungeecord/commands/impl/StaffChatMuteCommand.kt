package dev.austech.betterstaffchat.bungeecord.commands.impl

import dev.austech.betterstaffchat.bungeecord.commands.BSCBungeeCommand
import dev.austech.betterstaffchat.common.util.Config
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer

class StaffChatMuteCommand(name: String, aliases: List<String>): BSCBungeeCommand(name, "betterstaffchat.mutestaffchat", aliases) {
    override fun run(sender: CommandSender, args: Array<out String>) {
        if (sender.requirePlayer()) {
            return
        }

        if (sender !is ProxiedPlayer) {
            throw IllegalStateException("Sender is not a player!")
        }

        val mute: Boolean
        val mutedPlayers = plugin.dataWrapper.getMutedPlayers()

        if (args.isNotEmpty()) {
            if (args[0].equals("off", true)) {
                if (!mutedPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is not muted.")
                    return
                }

                mute = false
            } else if (args[0].equals("on", true)) {
                if (mutedPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is already muted.")
                    return
                }

                mute = true
            } else {
                mute = !mutedPlayers.contains(sender.uniqueId)
            }
        } else {
            mute = !mutedPlayers.contains(sender.uniqueId)
        }

        if (mute) {
            plugin.dataWrapper.addMutedPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.STAFFCHAT_MUTE_ON_MESSAGE.toString()))
        } else {
            plugin.dataWrapper.removeMutedPlayer(sender.uniqueId)
            legacyTell(plugin.config.getString(Config.Paths.STAFFCHAT_MUTE_OFF_MESSAGE.toString()))
        }

        return
    }
}