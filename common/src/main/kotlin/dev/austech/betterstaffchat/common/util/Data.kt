package dev.austech.betterstaffchat.common.util

import de.leonhard.storage.Json
import dev.austech.betterstaffchat.common.BSCPlugin
import java.io.File
import java.util.*

class Data(private val plugin: BSCPlugin) {
    private val dataFile: File = File(plugin.pluginDataFile, "data.json")

    class Wrapper(private val `data`: Json) {
        fun getToggledPlayers(): List<UUID> {
            return `data`.getOrSetDefault<List<String>>("toggledPlayers", listOf()).map { UUID.fromString(it) }
        }

        fun addToggledPlayer(uuid: UUID) {
            return `data`.set("toggledPlayers", getToggledPlayers().plus(uuid.toString()))
        }

        fun removeToggledPlayer(uuid: UUID) {
            return `data`.set("toggledPlayers", getToggledPlayers().minus(uuid.toString()))
        }

        fun getMutedPlayers(): List<UUID> {
            return `data`.getOrSetDefault<List<String>>("mutedPlayers", listOf()).map { UUID.fromString(it) }
        }

        fun addMutedPlayer(uuid: UUID) {
            return `data`.set("mutedPlayers", getMutedPlayers().plus(uuid.toString()))
        }

        fun removeMutedPlayer(uuid: UUID) {
            return `data`.set("mutedPlayers", getMutedPlayers().minus(uuid.toString()))
        }
    }

    fun load(): Json {
        plugin.pluginDataFile.mkdirs()

        if (!dataFile.exists()) {
            dataFile.createNewFile()
        }

        return Json(dataFile)
    }
}
