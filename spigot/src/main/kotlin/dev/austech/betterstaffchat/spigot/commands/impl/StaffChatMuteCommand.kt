package dev.austech.betterstaffchat.spigot.commands.impl

import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.commands.BSCSpigotCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffChatMuteCommand(name: String, description: String, usage: String, aliases: List<String>) : BSCSpigotCommand(name, description, usage, aliases) {
    override fun run(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.requirePermission("betterstaffchat.mutestaffchat")) {
            return true
        }

        if (!sender.requirePlayer()) {
            return true
        }

        if (sender !is Player) {
            throw IllegalStateException("Sender is not a player!")
        }

        val mute: Boolean
        val mutedPlayers = plugin.dataWrapper.getMutedPlayers()

        if (args.isNotEmpty()) {
            if (args[0].equals("off", true)) {
                if (!mutedPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is not muted.")
                    return true
                }

                mute = false
            } else if (args[0].equals("on", true)) {
                if (mutedPlayers.contains(sender.uniqueId)) {
                    errorTell("Your staffchat is already muted.")
                    return true
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

        return true
    }
}
