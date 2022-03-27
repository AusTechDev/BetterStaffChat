package dev.austech.betterstaffchat.spigot.util

import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.BSCSpigot
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PlayerUtil {
    fun getSenderName(sender: CommandSender): String {
        return if (sender is Player) sender.name
        else BSCSpigot.instance.config.getString(Config.Paths.STAFFCHAT_CONSOLE.toString()) ?: "Console"
    }

    private fun getMutedPlayers(): List<UUID> {
        return BSCSpigot.instance.dataWrapper.getMutedPlayers()
    }

    private fun getPermissionAudience(permission: String): Audience = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && it is Player && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getMixedAudience(permission: String): Audience = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && if (it is Player) !getMutedPlayers().contains(it.uniqueId) else true
    }

    fun getReceiveAudience(): Audience {
        return if (BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT_CONSOLE_LOG.toString())) getMixedAudience(
            "betterstaffchat.messages.read"
        )
        else getPermissionAudience("betterstaffchat.messages.read")
    }
}
