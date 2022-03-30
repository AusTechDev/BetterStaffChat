package dev.austech.betterstaffchat.spigot.util

import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.common.util.IPlayerUtil
import dev.austech.betterstaffchat.spigot.BSCSpigot
import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object PlayerUtil: IPlayerUtil {
    fun getSenderName(sender: CommandSender): String {
        return if (sender is Player) sender.name
        else BSCSpigot.instance.config.getString(Config.Paths.Staffchat.CONSOLE_NAME.toString()) ?: "Console"
    }

    private fun getMutedPlayers(): List<UUID> {
        return BSCSpigot.instance.dataWrapper.getMutedPlayers()
    }

    private fun getAudienceWithIgnored(permission: String, ignored: UUID) = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && it is Player && it.uniqueId != ignored && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getAudienceMixedWithIgnored(permission: String, ignored: UUID) = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && if (it is Player) it.uniqueId != ignored && !getMutedPlayers().contains(it.uniqueId) else true
    }

    fun getReceiveAudienceWithIgnored(ignored: UUID): Audience {
        return if (BSCSpigot.instance.config.getBoolean(Config.Paths.Staffchat.CONSOLE_LOG.toString())) getAudienceMixedWithIgnored(
            "betterstaffchat.messages.read",
            ignored
        )
        else getAudienceWithIgnored("betterstaffchat.messages.read", ignored)
    }

    private fun getPermissionAudience(permission: String): Audience = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && it is Player && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getMixedAudience(permission: String): Audience = BSCSpigot.instance.audience.filter {
        it.hasPermission(permission) && if (it is Player) !getMutedPlayers().contains(it.uniqueId) else true
    }

    override fun getReceiveAudience(): Audience {
        return if (BSCSpigot.instance.config.getBoolean(Config.Paths.Staffchat.CONSOLE_LOG.toString())) getMixedAudience(
            "betterstaffchat.messages.read"
        )
        else getPermissionAudience("betterstaffchat.messages.read")
    }
}