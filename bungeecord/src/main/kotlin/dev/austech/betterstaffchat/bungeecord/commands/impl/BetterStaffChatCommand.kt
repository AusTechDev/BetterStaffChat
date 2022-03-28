package dev.austech.betterstaffchat.bungeecord.commands.impl

import dev.austech.betterstaffchat.bungeecord.commands.BSCBungeeCommand
import net.md_5.bungee.api.CommandSender

class BetterStaffChatCommand: BSCBungeeCommand("betterstaffchat", null, listOf("bsc")) {
    override fun run(sender: CommandSender, args: Array<out String>) {
        when (args[0]) {
            "reload" -> {
                if (!sender.requirePermission("betterstaffchat.reload")) return
                warnTell("It is not recommended to forcefully reload BetterStaffChat. It will automatically reload when the config is edited.")
                plugin.config.forceReload()
            }
            else -> {
                legacyTell("&7Running &9BetterStaffChat &7by &6AusTech Development&7.")
            }
        }

        return
    }
}