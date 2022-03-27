package dev.austech.betterstaffchat.spigot.commands

import dev.austech.betterstaffchat.spigot.BSCSpigot
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class BSCSpigotCommand(name: String, description: String, usage: String, aliases: List<String>) : Command(name, description, usage, aliases) {
    private lateinit var sender: CommandSender
    protected lateinit var plugin: BSCSpigot

    abstract fun run(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean;

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        this.sender = sender;
        this.plugin = BSCSpigot.instance

        return run(sender, commandLabel, args)
    }

    fun CommandSender.isConsole(): Boolean {
        return this !is Player
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
        return this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s))
    }

    fun errorTell(s: String) {
        return legacyTell("&c$s")
    }

    fun warnTell(s: String) {
        legacyTell("&e$s")
    }
}
