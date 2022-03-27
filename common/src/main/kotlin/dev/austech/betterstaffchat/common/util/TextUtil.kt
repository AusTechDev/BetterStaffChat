package dev.austech.betterstaffchat.common.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object TextUtil {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    fun escapeTags(s: String): String {
        return miniMessage.escapeTags(s)
    }

    fun parseText(s: String): Component {
        return miniMessage.deserialize(s)
    }
}