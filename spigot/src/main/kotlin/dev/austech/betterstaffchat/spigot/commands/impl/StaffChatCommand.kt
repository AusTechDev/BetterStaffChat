package dev.austech.betterstaffchat.spigot.commands.impl

import dev.austech.betterstaffchat.common.PlayerMeta
import dev.austech.betterstaffchat.spigot.commands.BSCSpigotCommand
import dev.austech.betterstaffchat.spigot.util.PlayerUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffChatCommand(name: String, description: String, usage: String, aliases: List<String>) : BSCSpigotCommand(name, description, usage, aliases) {
    override fun run(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (!sender.requirePermission("betterstaffchat.messages.send")) {
            return true
        }

        if (args.isEmpty()) {
            errorTell("&cUsage: /$commandLabel <message...>")
            return true;
        }

        if (sender is Player && plugin.dataWrapper.getMutedPlayers().contains(sender.uniqueId)) {
            errorTell("Your staffchat is currently muted.")
            return true
        }

        plugin.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), args.joinToString(separator = " "), PlayerUtil.getSenderName(sender), "N/A", PlayerMeta(null, null))

        return true;
    }
}