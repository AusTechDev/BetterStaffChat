package dev.austech.betterstaffchat.common.discord.util

import de.leonhard.storage.Yaml
import de.leonhard.storage.sections.FlatFileSection
import dev.austech.betterstaffchat.common.util.Config
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.time.Instant
import java.util.*

object DiscordUtil {
    fun buildEmbed(section: FlatFileSection, replaceProvider: ((String) -> String)?): EmbedBuilder {
        val embedBuilder = EmbedBuilder()

        if (
            section.getString("author.name") != null &&
            section.getString("author.name").isNotBlank()
        ) {
            embedBuilder.setAuthor(
                applyPlaceholders(replaceProvider, section.getString("author.name")),
                if (section.getString("author.url") != null && section.getString("author.url").isNotBlank())
                    applyPlaceholders(replaceProvider, section.getString("author.url")) else null,
                if (section.getString("author.icon-url") != null && section.getString("author.icon-url").isNotBlank())
                    applyPlaceholders(replaceProvider, section.getString("author.icon-url")) else null,
            )
        }

        if (
            section.getString("body.title") != null &&
            section.getString("body.title").isNotBlank()
        ) {
            embedBuilder.setTitle(
                applyPlaceholders(replaceProvider, section.getString("body.title")),
                if (section.getString("body.url") != null && section.getString("body.url").isNotBlank())
                    applyPlaceholders(replaceProvider, section.getString("body.url"))
                else null
            )
        }

        embedBuilder.setDescription(
            applyPlaceholders(replaceProvider, section.getString("body.description"))
        )

        embedBuilder.setColor(Color.decode(section.getString("body.color")))

        if (
            section.getString("images.thumbnail") != null &&
            section.getString("images.thumbnail").isNotBlank()
        ) {
            embedBuilder.setThumbnail(
                applyPlaceholders(replaceProvider, section.getString("images.thumbnail"))
            )
        }

        if (
            section.getString("images.image") != null &&
            section.getString("images.image").isNotBlank()
        ) {
            embedBuilder.setImage(
                applyPlaceholders(replaceProvider, section.getString("images.image"))
            )
        }

        if (section.getBoolean("footer.timestamp")) {
            embedBuilder.setTimestamp(Instant.now())
        }

        if (
            section.getString("footer.text") != null &&
            section.getString("footer.text").isNotBlank()
        ) {
            embedBuilder.setFooter(
                applyPlaceholders(replaceProvider, section.getString("footer.text")),
                if (section.getString("footer.icon-url") != null && section.getString("footer.icon-url").isNotBlank())
                    applyPlaceholders(replaceProvider, section.getString("footer.icon-url"))
                else null
            )
        }

        if (section.get("fields") != null) {
            loop@ for (i in 1..25) {
                if (section.get("fields.$i") != null) {
                    embedBuilder.addField(
                        applyPlaceholders(replaceProvider, section.getString("fields.$i.name")),
                        applyPlaceholders(replaceProvider, section.getString("fields.$i.value")),
                        section.getBoolean("fields.$i.inline")
                    )
                } else break@loop
            }

            if (section.get("fields.26") != null) {
                throw IllegalArgumentException("Embed has 26+ fields, Discord only supports 25.")
            }
        }

        return embedBuilder
    }

    private fun applyPlaceholders(replaceProvider: ((String) -> String)?, s: String): String {
        if (replaceProvider == null) return s
        return replaceProvider.invoke(s)
    }

    fun isStaffChatChannel(section: FlatFileSection, channelId: String, guildId: String): Boolean {
        return getChannels(section, guildId, "staffchat").contains(channelId)
    }

    fun getChannel(section: FlatFileSection, type: String) {
        section.getStringList("events.$type")
    }

    fun getChannels(section: FlatFileSection, guildId: String, type: String): List<String> {
        return section.getStringList("$guildId.events.$type")
    }

    fun getAllChannels(config: Yaml, type: String): List<String> {
        val channels = arrayListOf<String>()
        val section = config.getSection(Config.Paths.Discord.BOT_CHANNELS.toString())

        section.singleLayerKeySet().map {
            channels.addAll(section.getStringList("$it.events.$type"))
        }

        return channels
    }
}