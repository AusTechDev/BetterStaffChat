package dev.austech.betterstaffchat.bungeecord.listeners

import dev.austech.betterstaffchat.bungeecord.BSCBungee
import dev.austech.betterstaffchat.bungeecord.util.PlayerUtil
import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.util.Config
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import java.util.concurrent.TimeUnit

class PlayerListener(private val plugin: Plugin): Listener {
    @EventHandler
    fun onPlayerChat(event: ChatEvent) {
        if (event.isCommand || event.isProxyCommand) return

        val player = event.sender as ProxiedPlayer

        if (player.hasPermission("betterstaffchat.messages.send")) {
            val prefix = BSCBungee.instance.config.getString(Config.Paths.STAFFCHAT.PREFIX.toString()) ?: "";

            if (event.message.startsWith(prefix) && event.message.length > prefix.length && prefix.isNotBlank() && !prefix.startsWith("/")) {
                if (PlayerUtil.isDisabledServer(player.server.info)) {
                    BSCBungee.instance.audience.player(player).sendMessage(Component.text("You cannot use staffchat on this server.").color(NamedTextColor.RED))
                    return
                }

                event.isCancelled = true;
                BSCBungee.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message.substring(prefix.length),
                    BSCMetadata(
                        BSCMetadata.Player(
                            player.uniqueId,
                            player.name
                        ),
                        BSCMetadata.Proxy(
                            PlayerUtil.getServer(player),
                            PlayerUtil.getRawServer(player)
                        )
                    )
                )
            } else if (BSCBungee.instance.dataWrapper.getToggledPlayers().contains(player.uniqueId) && BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.TOGGLE_ENABLED.toString())) {
                if (PlayerUtil.isDisabledServer(player.server.info)) {
                    BSCBungee.instance.audience.player(player).sendMessage(Component.text("You cannot use staffchat on this server.").color(NamedTextColor.RED))
                    return
                }

                event.isCancelled = true;
                BSCBungee.instance.staffChatUtil.sendMessage(PlayerUtil.getReceiveAudience(), event.message,
                    BSCMetadata(
                        BSCMetadata.Player(
                            player.uniqueId,
                            player.name
                        ),
                        BSCMetadata.Proxy(
                            PlayerUtil.getServer(player),
                            PlayerUtil.getRawServer(player)
                        )
                    )
                )
            }
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent) {
        if (!BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_JOIN_ENABLED.toString())) return
        if (event.player.hasPermission("betterstaffchat.messages.join")) {
            if (PlayerUtil.isDisabledServer(event.player.server.info)) {
                return
            }

            val aud: Audience = if (!BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_JOIN_SEE_OWN.toString()))
                PlayerUtil.getReceiveAudienceWithIgnored(event.player.uniqueId)
            else PlayerUtil.getReceiveAudience()

            ProxyServer.getInstance().scheduler.schedule(plugin, {
                BSCBungee.instance.staffChatUtil.sendJoinMessage(aud,
                    BSCMetadata(
                        BSCMetadata.Player(
                            event.player.uniqueId,
                            event.player.name
                        ),
                        BSCMetadata.Proxy(
                            PlayerUtil.getServer(event.player),
                            PlayerUtil.getRawServer(event.player)
                        )
                    )
                )
            }, 1, TimeUnit.SECONDS)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        if (!BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_LEAVE_ENABLED.toString())) return
        if (event.player.hasPermission("betterstaffchat.messages.leave")) {
            if (PlayerUtil.isDisabledServer(event.player.server.info)) {
                return
            }

            BSCBungee.instance.staffChatUtil.sendLeaveMessage(PlayerUtil.getReceiveAudience(),
                BSCMetadata(
                    BSCMetadata.Player(
                        event.player.uniqueId,
                        event.player.name
                    ),
                    BSCMetadata.Proxy(
                        PlayerUtil.getServer(event.player),
                        PlayerUtil.getRawServer(event.player)
                    )
                )
            )
        }
    }

    @EventHandler
    fun onPlayerSwitch(event: ServerSwitchEvent) {
        if (!BSCBungee.instance.config.getBoolean(Config.Paths.STAFFCHAT.EVENTS_SWITCH_ENABLED.toString())) return
        if (event.player.hasPermission("betterstaffchat.messages.switch")) {
            requireNotNull(event.from) { "From server is null! This means you are using an old version of FlameCord or Aegis." }

            if (PlayerUtil.isDisabledServer(event.player.server.info)) {
                return
            }

            BSCBungee.instance.staffChatUtil.sendSwitchMessage(PlayerUtil.getReceiveAudience(), PlayerUtil.getServerReplacement(event.from), PlayerUtil.getServer(event.player),
                BSCMetadata(
                    BSCMetadata.Player(
                        event.player.uniqueId,
                        event.player.name
                    ),
                    BSCMetadata.Proxy(
                        PlayerUtil.getServer(event.player),
                        PlayerUtil.getRawServer(event.player)
                    )
                )
            )
        }
    }
}