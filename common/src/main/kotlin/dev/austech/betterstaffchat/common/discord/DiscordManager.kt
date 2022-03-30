package dev.austech.betterstaffchat.common.discord

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.impl.bot.DiscordEmbedProvider
import dev.austech.betterstaffchat.common.discord.impl.bot.DiscordTextProvider
import dev.austech.betterstaffchat.common.util.Config

class DiscordManager(private val plugin: BSCPlugin) {
    lateinit var discordClient: DiscordClient
    private val messageProviderMap = mutableMapOf<String, DiscordMessageProvider>()

    init {
        start()
    }

    fun setupMessageProviderForBot() {
        arrayOf("staffchat", "join", "leave", "switch").forEach {
            plugin.config.getString(Config.Paths.Discord.Message(it).mode).let { m ->
                if (m == "embed") messageProviderMap[it] = DiscordEmbedProvider(discordClient.jda)
                else if (m == "text") messageProviderMap[it] = DiscordTextProvider(discordClient.jda)
            }
        }
    }

    fun getMessageProvider(mode: String): DiscordMessageProvider? {
        return messageProviderMap[mode]
    }

    private fun start() {
        plugin.config.getBoolean(Config.Paths.Discord.ENABLED.toString())
            .takeIf { it }
            ?.let {
                when (plugin.config.getString(Config.Paths.Discord.TYPE.toString())) {
                    "bot" -> {
                        discordClient = DiscordClient(plugin)
                        setupMessageProviderForBot()
                    }
                    "webhook" -> {

                    }
                    else -> {}
                }
            }
    }
}