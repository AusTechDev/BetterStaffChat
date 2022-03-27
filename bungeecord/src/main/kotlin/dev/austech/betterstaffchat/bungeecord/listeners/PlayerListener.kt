package dev.austech.betterstaffchat.bungeecord.listeners

import dev.austech.betterstaffchat.bungeecord.BSCBungee
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.PlayerMeta
import dev.austech.betterstaffchat.common.util.Config
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.concurrent.TimeUnit

class PlayerListener: Listener {
    @EventHandler
    fun onPlayerChat(event: ChatEvent) {
        if (event.isCommand || event.isProxyCommand) return

        val player = event.sender as ProxiedPlayer

        if (player.hasPermission("betterstaffchat.messages.send")) {
            val prefix = BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT_PREFIX.toString()) ?: "";

            if (event.message.startsWith(prefix) && event.message.length > prefix.length && prefix.isNotBlank() && !prefix.startsWith("/")) {
                event.isCancelled = true;
                BSCBungee.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message.substring(prefix.length), player.name, PlayerUtil.getServer(player), PlayerMeta(null, null))
            } else if (BSCBungee.instance.dataWrapper.getToggledPlayers().contains(player.uniqueId)) {
                event.isCancelled = true;
                BSCBungee.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message, player.name, PlayerUtil.getServer(player), PlayerMeta(null, null))
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.join")) {
            ProxyServer.getInstance().scheduler.schedule(BSCBungee.instance, {
                BSCBungee.instance.staffChatUtil.sendJoinMessage(PlayerUtil.getReceiveAudience(), event.player.name, PlayerUtil.getServer(event.player), PlayerMeta(null, null))
            }, 1, TimeUnit.SECONDS)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            BSCBungee.instance.staffChatUtil.sendLeaveMessage(PlayerUtil.getReceiveAudience(), event.player.name, PlayerUtil.getServer(event.player), PlayerMeta(null, null))
        }
    }

    @EventHandler
    fun onPlayerSwitch(event: ServerSwitchEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.switch")) {
            requireNotNull(event.from) { "From server is null! This means you are using an old version of FlameCord or Aegis." }
            BSCBungee.instance.staffChatUtil.sendSwitchMessage(PlayerUtil.getReceiveAudience(), event.player.name, PlayerUtil.getServerReplacement(event.from), PlayerUtil.getServer(event.player), PlayerMeta(null, null))
        }
    }
}
