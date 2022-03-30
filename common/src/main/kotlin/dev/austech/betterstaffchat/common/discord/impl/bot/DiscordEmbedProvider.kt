package dev.austech.betterstaffchat.common.discord.impl.bot

import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.DiscordMessageProvider
import dev.austech.betterstaffchat.common.discord.util.DiscordUtil
import dev.austech.betterstaffchat.common.util.Config
import net.dv8tion.jda.api.JDA

class DiscordEmbedProvider(private val jda: JDA): DiscordMessageProvider {
    override fun handleMessage(plugin: BSCPlugin, type: String, meta: BSCMetadata, placeholderProvider: (String) -> String) {
        val messages = Config.Paths.Discord.Message(type)

        val embed = DiscordUtil.buildEmbed(plugin.config.getSection(messages.embed)) {
            placeholderProvider(plugin.staffChatUtil.replacePlaceholders(it, meta))
        }.build()

        DiscordUtil.getAllChannels(plugin.config, type).map {
            jda.getTextChannelById(it)
        }.forEach {
            if (it == null) throw NullPointerException("Text channel could not be resolved!")
            it.sendMessageEmbeds(embed).queue()
        }
    }
}