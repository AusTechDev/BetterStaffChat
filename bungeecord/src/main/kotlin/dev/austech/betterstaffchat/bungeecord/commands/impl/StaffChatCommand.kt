package dev.austech.betterstaffchat.bungeecord.commands.impl

import dev.austech.betterstaffchat.bungeecord.commands.BSCBungeeCommand
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.BSCMetadata
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

class StaffChatCommand(name: String, aliases: List<String>) :
    BSCBungeeCommand(name, "betterstaffchat.messages.send", aliases) {
    override fun run(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            errorTell("Usage: /$name <message...>")
            return;
        }

        if (sender is ProxiedPlayer) {
            if (plugin.dataWrapper.getMutedPlayers().contains(sender.uniqueId)) {
                errorTell("Your staffchat is currently muted.")
                return;
            }

            if (PlayerUtil.isDisabledServer(sender.server.info)) {
                errorTell("You cannot use staffchat on this server.")
                return;
            }
        }


        plugin.staffChatUtil.sendMessage(
            PlayerUtil.getReceiveAudience(), args.joinToString(separator = " "),
            BSCMetadata(
                BSCMetadata.Player(
                    if (sender is ProxiedPlayer) sender.uniqueId else null,
                    PlayerUtil.getSenderName(sender)
                ),
                BSCMetadata.Proxy(
                    PlayerUtil.getServer(sender),
                    PlayerUtil.getRawServer(sender)
                )
            )
        )
    }
}