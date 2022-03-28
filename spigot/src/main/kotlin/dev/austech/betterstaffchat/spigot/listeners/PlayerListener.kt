package dev.austech.betterstaffchat.spigot.listeners

import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.util.Config
import dev.austech.betterstaffchat.spigot.BSCSpigot
import dev.austech.betterstaffchat.spigot.util.PlayerUtil
import net.kyori.adventure.audience.Audience
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.player.hasPermission("betterstaffchat.messages.send")) {
            val prefix = BSCSpigot.instance.config.getString(Config.Paths.STAFFCHAT.PREFIX.toString()) ?: ""

            if (event.message.startsWith(prefix) && event.message.length > prefix.length && prefix.isNotBlank()) {
                event.isCancelled = true;
                BSCSpigot.instance.staffChatUtil.sendMessage(
                    PlayerUtil.getReceiveAudience(), event.message.substring(prefix.length),
                    BSCMetadata(
                        BSCMetadata.Player(
                            event.player.uniqueId,
                            PlayerUtil.getSenderName(event.player)
                        ),
                        BSCMetadata.Spigot(
                            event.player.world.name,
                        )
                    )
                )
            } else if (BSCSpigot.instance.dataWrapper.getToggledPlayers()
                    .contains(event.player.uniqueId) && BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.TOGGLE_ENABLED.toString())
            ) {
                event.isCancelled = true;
                BSCSpigot.instance.staffChatUtil.sendMessage(
                    PlayerUtil.getReceiveAudience(), event.message,
                    BSCMetadata(
                        BSCMetadata.Player(
                            event.player.uniqueId,
                            PlayerUtil.getSenderName(event.player)
                        ),
                        BSCMetadata.Spigot(
                            event.player.world.name,
                        )
                    )
                )
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_JOIN_ENABLED.toString())) return
        if (event.player.hasPermission("betterstaffchat.messages.join")) {
            val aud: Audience =
                if (!BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_JOIN_SEE_OWN.toString()))
                    PlayerUtil.getReceiveAudienceWithIgnored(event.player.uniqueId)
                else PlayerUtil.getReceiveAudience()

            BSCSpigot.instance.staffChatUtil.sendJoinMessage(
                aud,
                BSCMetadata(
                    BSCMetadata.Player(
                        event.player.uniqueId,
                        PlayerUtil.getSenderName(event.player)
                    ),
                    BSCMetadata.Spigot(
                        event.player.world.name,
                    )
                )
            )
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (!BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_LEAVE_ENABLED.toString())) return
        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            BSCSpigot.instance.staffChatUtil.sendLeaveMessage(
                PlayerUtil.getReceiveAudience(),
                BSCMetadata(
                    BSCMetadata.Player(
                        event.player.uniqueId,
                        PlayerUtil.getSenderName(event.player)
                    ),
                    BSCMetadata.Spigot(
                        event.player.world.name,
                    )
                )
            )
        }
    }

    @EventHandler
    fun onPlayerSwitch(event: PlayerChangedWorldEvent) {
        if (!BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_SWITCH_ENABLED.toString())) return
        if (!BSCSpigot.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_SWITCH_USE_WORLDS.toString())) return

        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            BSCSpigot.instance.staffChatUtil.sendSwitchMessage(
                PlayerUtil.getReceiveAudience(), event.from.name, event.player.world.name,
                BSCMetadata(
                    BSCMetadata.Player(
                        event.player.uniqueId,
                        PlayerUtil.getSenderName(event.player)
                    ),
                    BSCMetadata.Spigot(
                        event.player.world.name,
                    )
                )
            )
        }
    }
}