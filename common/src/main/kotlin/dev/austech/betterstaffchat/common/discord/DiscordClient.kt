package dev.austech.betterstaffchat.common.discord

import dev.austech.betterstaffchat.common.BSCPlugin
import dev.austech.betterstaffchat.common.discord.impl.bot.DiscordListener
import net.dv8tion.jda.api.JDABuilder
import dev.austech.betterstaffchat.common.util.Config
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class DiscordClient(private val plugin: BSCPlugin) {
    val jda = JDABuilder.create(plugin.config.getString(Config.Paths.Discord.BOT_TOKEN.toString()), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
        .addEventListeners(DiscordListener(plugin))
        .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
        .build()

    init {
        jda.awaitReady()
        setup()
    }

    fun setup() {
        if (plugin.config.getBoolean(Config.Paths.Discord.BOT_ACTIVITY.toString())) {
            jda.presence.activity = Activity.of(
                Activity.ActivityType.valueOf(plugin.config.getString(Config.Paths.Discord.BOT_ACTIVITY_TYPE.toString()).uppercase()),
                plugin.config.getString(Config.Paths.Discord.BOT_ACTIVITY_STATUS.toString()),
                "https://twitch.tv/"
            )
        }

        jda.presence.setStatus(OnlineStatus.fromKey(plugin.config.getString(Config.Paths.Discord.BOT_STATUS.toString())))
    }
}