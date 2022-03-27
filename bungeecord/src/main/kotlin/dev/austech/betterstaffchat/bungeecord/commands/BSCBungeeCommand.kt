package dev.austech.betterstaffchat.bungeecord.commands

import dev.austech.betterstaffchat.bungeecord.BSCBungee
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

abstract class BSCBungeeCommand(name: String, permission: String?, aliases: List<String>): Command(name, permission, *aliases.toTypedArray()) {
    private lateinit var sender: CommandSender
    protected lateinit var plugin: BSCBungee

    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        requireNotNull(sender) { "Sender cannot be null" }

        this.sender = sender
        this.plugin = BSCBungee.instance

        run(sender, args ?: arrayOf())
    }

    abstract fun run(sender: CommandSender, args: Array<out String>)

    fun CommandSender.isConsole(): Boolean {
        return this !is ProxiedPlayer
    }

    fun CommandSender.requirePlayer(): Boolean {
        if (this.isConsole()) {
            errorTell("You must be a player to use this command.")
            return false
        }
        return true
    }

    fun CommandSender.requirePermission(permission: String): Boolean {
        return requirePermission(permission, "You do not have permission.")
    }

    fun CommandSender.requirePermission(permission: String, message: String): Boolean {
        if (!hasPermission(permission)) {
            errorTell(message)
            return false
        }
        return true
    }

    fun legacyTell(s: String) {
        return this.sender.sendMessage(TextComponent(*TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s))))
    }

    fun errorTell(s: String) {
        return legacyTell("&c$s")
    }

    fun warnTell(s: String) {
        legacyTell("&e$s")
    }
}
