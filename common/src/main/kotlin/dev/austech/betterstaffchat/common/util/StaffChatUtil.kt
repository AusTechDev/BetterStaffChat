package dev.austech.betterstaffchat.common.util

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.BSCMetadata
import net.kyori.adventure.audience.Audience
import java.util.*

class StaffChatUtil(private val plugin: BSCPlugin) {

    lateinit var replaceProvider: (UUID, String) -> String

    constructor(plugin: BSCPlugin, consumer: (u: UUID, s: String) -> String) : this(plugin) {
        replaceProvider = consumer
    }

    private fun replacePlaceholders(s: String, meta: BSCMetadata): String {
        var newString = s
            .replace("%player_name%", meta.playerMeta.name)

        if (meta.playerMeta.uuid != null) {
            newString = newString.replace("%player_uuid%", meta.playerMeta.uuid.toString())
        }

        if (meta.proxyMeta != null) {
            val proxy: BSCMetadata.Proxy = meta.proxyMeta!!
            newString = newString
                .replace("%player_server%", proxy.server)
                .replace("%player_server_raw%", proxy.serverRaw)
        }

        if (meta.spigotMeta != null) {
            val spigot: BSCMetadata.Spigot = meta.spigotMeta!!

            if (spigot.world != null) {
                newString = newString
                    .replace("%player_world%", spigot.world)
            }
        }

        if (meta.luckpermsMeta != null) {
            val luckperms: BSCMetadata.LuckPerms = meta.luckpermsMeta!!

            if (luckperms.group != null && luckperms.groupDisplayName != null) {
                newString = newString
                    .replace("%luckperms_group%", luckperms.group)
                    .replace("%luckperms_group_displayname%", luckperms.groupDisplayName)
            }

            if (luckperms.prefix != null) {
                newString = newString.replace("%luckperms_prefix%", luckperms.prefix)
            }

            if (luckperms.suffix != null) {
                newString = newString.replace("%luckperms_suffix%", luckperms.suffix)
            }
        }

        if (meta.playerMeta.uuid != null) {
            replaceProvider.invoke(meta.playerMeta.uuid, newString)
        }

        return newString;
    }

    fun sendMessage(audience: Audience, message: String, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT.FORMAT.toString()) ?: return;
        val removeColorCodes = plugin.config.getBoolean(Config.Paths.STAFFCHAT.STRIP_COLOR_CODES.toString());

        configMessage = replacePlaceholders(configMessage, meta)
            .replace("%message%",
                if (removeColorCodes)
                    TextUtil.escapeTags(message)
                else message
            )

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendJoinMessage(audience: Audience, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT.EVENTS_JOIN_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendLeaveMessage(audience: Audience, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT.EVENTS_LEAVE_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendSwitchMessage(audience: Audience, from: String, to: String, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT.EVENTS_SWITCH_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)
        configMessage = configMessage
            .replace("%from%", from)
            .replace("%to%", to)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }
}