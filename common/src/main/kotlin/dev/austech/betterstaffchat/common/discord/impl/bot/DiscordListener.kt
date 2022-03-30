package dev.austech.betterstaffchat.common.discord.impl.bot

import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.util.DiscordUtil
import dev.austech.betterstaffchat.common.util.Config
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordListener(private val plugin: BSCPlugin) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channelType != ChannelType.TEXT && !event.isFromGuild) return
        if (event.author.isBot) return
        if (!DiscordUtil.isStaffChatChannel(
                plugin.config.getSection(Config.Paths.Discord.BOT_CHANNELS.toString()),
                event.channel.id,
                event.guildChannel.guild.id
            )
        ) return;

        val formattedUsername = plugin.config.getString(Config.Paths.Discord.NAME_FORMAT.toString())
            .replace("%username%", event.author.name)
            .replace("%discriminator%", event.author.discriminator)
            .replace("%tag%", event.author.asTag)
            .replace("%nickname", event.member!!.effectiveName)

        val message = StringBuilder(event.message.contentStripped.trim())

        if (event.message.attachments.isNotEmpty()) {
            if (message.isBlank()) {
                message.append("[Attachments] ")

                message.append(
                    event.message.attachments.joinToString(" ") {
                        it.url
                    }
                )
            }
        }

        plugin.staffChatUtil.sendMessage(
            plugin.playerUtil.getReceiveAudience(),
            message.toString(),
            BSCMetadata(BSCMetadata.Discord(formattedUsername))
        )
    }
}
