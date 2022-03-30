package dev.austech.betterstaffchat.common.discord.impl.bot

import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.DiscordMessageProvider
import dev.austech.betterstaffchat.common.discord.util.DiscordUtil
import dev.austech.betterstaffchat.common.util.Config
import net.dv8tion.jda.api.JDA

class DiscordTextProvider(private val jda: JDA): DiscordMessageProvider {
    override fun handleMessage(plugin: BSCPlugin, type: String, meta: BSCMetadata, placeholderProvider: (String) -> String) {
        val messages = Config.Paths.Discord.Message(type)
        val message = placeholderProvider(plugin.staffChatUtil.replacePlaceholders(messages.text, meta))

        DiscordUtil.getAllChannels(plugin.config, type).map {
            jda.getTextChannelById(it)
        }.forEach {
            if (it == null) throw NullPointerException("Text channel could not be resolved!")
            it.sendMessage(message).queue()
        }
    }
}