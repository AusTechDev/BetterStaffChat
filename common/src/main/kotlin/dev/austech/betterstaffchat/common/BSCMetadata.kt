package dev.austech.betterstaffchat.common

import lombok.AllArgsConstructor
import java.util.UUID

class BSCMetadata {
    val playerMeta: Player
    var luckpermsMeta: LuckPerms? = null
    var proxyMeta: Proxy? = null
    var spigotMeta: Spigot? = null


    constructor(playerMeta: Player) {
        this.playerMeta = playerMeta
    }

    constructor(playerMeta: Player, luckpermsMeta: LuckPerms) {
        this.playerMeta = playerMeta
        this.luckpermsMeta = luckpermsMeta
    }

    constructor(playerMeta: Player, proxyMeta: Proxy) {
        this.playerMeta = playerMeta
        this.proxyMeta = proxyMeta
    }

    constructor(playerMeta: Player, spigotMeta: Spigot) {
        this.playerMeta = playerMeta
        this.spigotMeta = spigotMeta
    }

    constructor(playerMeta: Player, proxyMeta: Proxy, luckpermsMeta: LuckPerms) {
        this.playerMeta = playerMeta
        this.luckpermsMeta = luckpermsMeta
        this.proxyMeta = proxyMeta
    }

    constructor(playerMeta: Player, spigotMeta: Spigot, luckpermsMeta: LuckPerms) {
        this.playerMeta = playerMeta
        this.luckpermsMeta = luckpermsMeta
        this.spigotMeta = spigotMeta
    }

    @AllArgsConstructor
    class LuckPerms(
        val prefix: String?,
        val suffix: String?,
        val group: String?,
        val groupDisplayName: String?,
    )

    @AllArgsConstructor
    class Player(
        val uuid: UUID?,
        val name: String,
    )

    @AllArgsConstructor
    class Proxy(
        val server: String,
        val serverRaw: String
    )

    @AllArgsConstructor
    class Spigot(
        val world: String?
    )
}