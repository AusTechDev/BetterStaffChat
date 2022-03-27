package dev.austech.betterstaffchat.spigot.listeners

import dev.austech.betterstaffchat.common.PlayerMeta
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.BSCSpigot
import dev.austech.betterstaffchat.spigot.util.PlayerUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener: Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.send")) {
            val prefix = BSCSpigot.instance.config.getString(Config.Paths.STAFFCHAT_PREFIX.toString()) ?: "";

            if (event.message.startsWith(prefix) && event.message.length > prefix.length && prefix.isNotBlank()) {
                event.isCancelled = true;
                BSCSpigot.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message.substring(prefix.length), event.player.name, "N/A", PlayerMeta(null, null))
            } else if (BSCSpigot.instance.dataWrapper.getToggledPlayers().contains(event.player.uniqueId)) {
                event.isCancelled = true;
                BSCSpigot.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message, event.player.name, "N/A", PlayerMeta(null, null))
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.join")) {
            BSCSpigot.instance.staffChatUtil.sendJoinMessage(PlayerUtil.getReceiveAudience(), event.player.name, "N/A", PlayerMeta(null, null))
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            BSCSpigot.instance.staffChatUtil.sendLeaveMessage(PlayerUtil.getReceiveAudience(), event.player.name, "N/A", PlayerMeta(null, null))
        }
    }

    @EventHandler
    fun onPlayerSwitch(event: PlayerChangedWorldEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            BSCSpigot.instance.staffChatUtil.sendSwitchMessage(PlayerUtil.getReceiveAudience(), event.player.name, event.from.name, event.player.world.name, PlayerMeta(null, null))
        }
    }
}
