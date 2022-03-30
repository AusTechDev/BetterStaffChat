package dev.austech.betterstaffchat.common.util

import net.kyori.adventure.audience.Audience

interface IPlayerUtil {
    fun getReceiveAudience(): Audience
}