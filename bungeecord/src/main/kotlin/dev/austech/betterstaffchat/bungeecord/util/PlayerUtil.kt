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
        else BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT.CONSOLE_NAME.toString())
    }

    private fun getMutedPlayers(): List<UUID> {
        return BSCBungee.instance.dataWrapper.getMutedPlayers()
    }

    private fun getAudienceWithIgnored(permission: String, ignored: UUID) = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && it is ProxiedPlayer && it.uniqueId != ignored && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getAudienceMixedWithIgnored(permission: String, ignored: UUID) = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && if (it is ProxiedPlayer) it.uniqueId != ignored && !getMutedPlayers().contains(it.uniqueId) else true
    }

    fun getReceiveAudienceWithIgnored(ignored: UUID): Audience {
        return if (BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.CONSOLE_LOG.toString())) getAudienceMixedWithIgnored(
            "betterstaffchat.messages.read",
            ignored
        )
        else getAudienceWithIgnored("betterstaffchat.messages.read", ignored)
    }

    private fun getPermissionAudience(permission: String): Audience = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && it is ProxiedPlayer && !getMutedPlayers().contains(it.uniqueId)
    }

    private fun getMixedAudience(permission: String): Audience = BSCBungee.instance.audience.filter {
        it.hasPermission(permission) && if (it is ProxiedPlayer) !getMutedPlayers().contains(it.uniqueId) else true
    }

    fun getReceiveAudience(): Audience {
        return if (BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.CONSOLE_LOG.toString())) getMixedAudience(
            "betterstaffchat.messages.read"
        )
        else getPermissionAudience("betterstaffchat.messages.read")
    }

    fun getServer(sender: CommandSender): String {
        return if (sender !is ProxyServer) BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT.CONSOLE_SERVER.toString())
        else getServerReplacement((sender as ProxiedPlayer).server.info)
    }

    fun getRawServer(sender: CommandSender): String {
        return if (sender !is ProxyServer) BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT.CONSOLE_SERVER.toString())
        else (sender as ProxiedPlayer).server.info.name
    }

    fun getServerReplacement(info: ServerInfo): String {
        BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT.SERVER_REPLACEMENTS.toString() + "." + info.name)?.let {
            return it
        }

        return info.name
    }

    fun isDisabledServer(info: ServerInfo): Boolean {
        return BSCBungee.instance.config.getStringList(Config.Paths.STAFFCHAT.DISABLED_SERVERS.toString())?.contains(info.name) ?: false
    }
}