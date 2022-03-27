package dev.austech.betterstaffchat.bungeecord.util

import dev.austech.betterstaffchat.bungeecord.BSCBungee
import dev.austech.betterstaffchat.common.util.Config
import net.kyori.adventure.audience.Audience
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object PlayerUtil {
    fun getSenderName(sender: CommandSender): String {
        return if (sender is ProxiedPlayer) sender.name
        else BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT_CONSOLE.toString())
    }

    private fun getMutedPlayers(): List<UUID> {
        return BSCBungee.instance.dataWrapper.getMutedPlayers()
    }

    private fun getPermissionAudience(permission: String): Audience = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && it is ProxiedPlayer && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getMixedAudience(permission: String): Audience = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && if (it is ProxiedPlayer) !getMutedPlayers().contains(it.uniqueId) else true
    }

    fun getReceiveAudience(): Audience {
        return if (BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT_CONSOLE_LOG.toString())) getMixedAudience(
            "betterstaffchat.messages.read"
        )
        else getPermissionAudience("betterstaffchat.messages.read")
    }

    fun getServer(sender: CommandSender): String {
        return if (sender !is ProxyServer) BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT_CONSOLE_SERVER.toString())
        else getServerReplacement((sender as ProxiedPlayer).server.info)
    }

    fun getServerReplacement(info: ServerInfo): String {
        BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT_SERVER_REPLACEMENTS.toString() + "." + info.name)?.let {
            return it
        }

        return info.name
    }
}