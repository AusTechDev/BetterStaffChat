package dev.austech.betterstaffchat.bungeecord.commands.impl

import dev.austech.betterstaffchat.bungeecord.commands.BSCBungeeCommand
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.PlayerMeta
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer

class StaffChatCommand(name: String, aliases: List<String>): BSCBungeeCommand(name, "betterstaffchat.messages.send", aliases) {
    override fun run(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            errorTell("Usage: /$name <message...>")
            return;
        }

        if (sender is ProxiedPlayer && plugin.dataWrapper.getMutedPlayers().contains(sender.uniqueId)) {
            errorTell("Your staffchat is currently muted.")
            return;
        }

        plugin.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), args.joinToString(separator = " "), PlayerUtil.getSenderName(sender), PlayerUtil.getServer(sender), PlayerMeta(null, null))
    }
}
