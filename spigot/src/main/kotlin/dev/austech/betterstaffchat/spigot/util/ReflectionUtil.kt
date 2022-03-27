package dev.austech.betterstaffchat.spigot.util

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap

object ReflectionUtil {
    private val serverClass by lazy {
        "org.bukkit.craftbukkit." + Bukkit.getServer().javaClass.`package`.name.substring(23) + ".CraftServer"
    }

    private fun getCommandMap(): SimpleCommandMap? {
        return try {
            Class.forName(serverClass).getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun registerCommand(command: Command) {
        getCommandMap()?.register(command.name, "betterstaffchat", command)
        if (!command.isRegistered) {
            throw RuntimeException("Failed to register command: " + command.name);
        }
    }
}
