package dev.austech.betterstaffchat.spigot.commands.impl

import dev.austech.betterstaffchat.spigot.commands.BSCSpigotCommand
import org.bukkit.command.CommandSender

class BetterStaffChatCommand: BSCSpigotCommand("betterstaffchat", "The main command for BetterStaffChat", "", listOf("bsc")) {
    override fun run(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        when (args[0]) {
            "reload" -> {
                if (!sender.requirePermission("betterstaffchat.reload")) return true;
                warnTell("It is not recommended to forcefully reload BetterStaffChat. It will automatically reload when the config is edited.")
                plugin.config.forceReload()
            }
            else -> {
                legacyTell("&7Running &9BetterStaffChat &7by &6AusTech Development&7.")
            }
        }

        return true;
    }
}