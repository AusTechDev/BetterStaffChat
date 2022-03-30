package dev.austech.betterstaffchat.common.util

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.BSCMetadata
import net.kyori.adventure.audience.Audience
import java.util.*

class StaffChatUtil(private val plugin: BSCPlugin) {

    private lateinit var replaceProvider: (UUID, String) -> String

    private val discordEnabled: Boolean
        get() = plugin.config.getBoolean(Config.Paths.Discord.ENABLED.toString())

    constructor(plugin: BSCPlugin, consumer: (u: UUID, s: String) -> String) : this(plugin) {
        replaceProvider = consumer
    }

    fun replacePlaceholders(s: String, meta: BSCMetadata): String {
        if (meta.discordMeta != null)
            return s
                .replace("%player_name%", meta.discordMeta!!.displayName ?: "")
                .replace("%player_server%", "Discord")
                .replace("%player_server_raw%", "Discord")

        if (meta.playerMeta == null) return s

        val playerMeta = meta.playerMeta!!

        var newString = s
            .replace("%player_name%", playerMeta.name)

        if (playerMeta.uuid != null) {
            newString = newString.replace("%player_uuid%", playerMeta.uuid.toString())
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

        if (playerMeta.uuid != null) {
            replaceProvider.invoke(playerMeta.uuid, newString)
        }

        return newString;
    }

    fun sendMessage(audience: Audience, message: String, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.Staffchat.FORMAT.toString()) ?: return;
        val removeColorCodes = plugin.config.getBoolean(Config.Paths.Staffchat.STRIP_COLOR_CODES.toString());

        configMessage = replacePlaceholders(configMessage, meta)
            .replace("%message%",
                if (removeColorCodes)
                    TextUtil.escapeTags(message)
                else message
            )

        audience.sendMessage(TextUtil.parseText(configMessage))

        if (discordEnabled && meta.discordMeta == null) {
            plugin.discordManager.getMessageProvider("staffchat")?.handleMessage(plugin, "staffchat", meta) {
                it.replace("%message%", message)
            }
        }
    }

    fun sendJoinMessage(audience: Audience, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.Staffchat.EVENTS_JOIN_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))

        if (discordEnabled) {
            plugin.discordManager.getMessageProvider("staffchat")?.handleMessage(plugin, "staffchat", meta) {
                    it
            }
        }
    }

    fun sendLeaveMessage(audience: Audience, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.Staffchat.EVENTS_LEAVE_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))

        if (discordEnabled) {
            plugin.discordManager.getMessageProvider("staffchat")?.handleMessage(plugin, "staffchat", meta) {
                it
            }
        }
    }

    fun sendSwitchMessage(audience: Audience, from: String, to: String, meta: BSCMetadata) {
        var configMessage: String = plugin.config.getString(Config.Paths.Staffchat.EVENTS_SWITCH_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, meta)
        configMessage = configMessage
            .replace("%from%", from)
            .replace("%to%", to)

        audience.sendMessage(TextUtil.parseText(configMessage))

        if (discordEnabled) {
            plugin.discordManager.getMessageProvider("staffchat")?.handleMessage(plugin, "staffchat", meta) {
                it.replace("%from%", from).replace("%to%", to)
            }
        }
    }
}