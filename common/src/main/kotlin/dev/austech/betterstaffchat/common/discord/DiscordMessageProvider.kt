package dev.austech.betterstaffchat.common.discord

import dev.austech.betterstaffchat.common.BSCMetadata
import dev.austech.betterstaffchat.common.BSCPlugin

interface DiscordMessageProvider {
    fun handleMessage(plugin: BSCPlugin, type: String, meta: BSCMetadata, placeholderProvider: (String) -> String)
//    fun handleJoin(plugin: BSCPlugin, s: String, meta: BSCMetadata)
//    fun handleLeave(plugin: BSCPlugin, s: String, meta: BSCMetadata)
//    fun handleSwitch(plugin: BSCPlugin, s: String, from: String, to: String, meta: BSCMetadata)
}