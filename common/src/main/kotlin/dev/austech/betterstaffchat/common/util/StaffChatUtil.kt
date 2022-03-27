package dev.austech.betterstaffchat.common.util

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.PlayerMeta
import net.kyori.adventure.audience.Audience

class StaffChatUtil(private val plugin: BSCPlugin) {
    private fun replacePlaceholders(s: String, playerName: String, server: String, meta: PlayerMeta?): String {
        var newString = s
            .replace("%player_name%", playerName)
            .replace("%server%", server);

        if (meta != null) {
            if (meta.prefix != null)
                newString = newString
                    .replace("%luckperms_prefix%", meta.prefix)
                    .replace("%prefix%", meta.prefix)

            if (meta.suffix != null)
                newString = newString
                    .replace("%luckperms_suffix%", meta.suffix)
                    .replace("%suffix%", meta.suffix)
        }

        return newString;
    }

    fun sendMessage(audience: Audience, message: String, playerName: String, server: String, meta: PlayerMeta?) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT_FORMAT.toString()) ?: return;
        val removeColorCodes = plugin.config.getBoolean(Config.Paths.STAFFCHAT_STRIP_COLOR_CODES.toString());

        configMessage = replacePlaceholders(configMessage, playerName, server, meta)
            .replace("%message%",
                if (removeColorCodes)
                    TextUtil.escapeTags(message)
                else message
            )

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendJoinMessage(audience: Audience, playerName: String, server: String, meta: PlayerMeta?) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT_JOIN_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, playerName, server, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendLeaveMessage(audience: Audience, playerName: String, server: String, meta: PlayerMeta?) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT_LEAVE_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, playerName, server, meta)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }

    fun sendSwitchMessage(audience: Audience, playerName: String, from: String, to: String, meta: PlayerMeta?) {
        var configMessage: String = plugin.config.getString(Config.Paths.STAFFCHAT_SWITCH_MESSAGE.toString()) ?: return;
        configMessage = replacePlaceholders(configMessage, playerName, from, meta)
        configMessage = configMessage
            .replace("%from%", from)
            .replace("%to%", to)

        audience.sendMessage(TextUtil.parseText(configMessage))
    }
}
