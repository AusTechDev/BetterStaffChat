package dev.austech.betterstaffchat.common.update

import de.leonhard.storage.shaded.json.JSONObject // might as well steal it
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class UpdateChecker(private val plugin: Any) {
    val needsUpdate: Boolean
    val currentVersion: String
    val latestVersion: String

    init {
        currentVersion = pluginVersion
        latestVersion = fetchUpdate() ?: "ERROR"
        needsUpdate = latestVersion != currentVersion
    }

    private val pluginVersion: String
        get() {
            val method = plugin.javaClass.getMethod("getDescription")
            method.isAccessible = true
            val objDescription = method.invoke(plugin)
            val versionMethod = objDescription.javaClass.getDeclaredMethod("getVersion")
            versionMethod.isAccessible = true
            return versionMethod.invoke(objDescription) as String? ?: ""
        }

    private fun fetchUpdate(): String? {
        val url = URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=91991")
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 5_000
        connection.connectTimeout = 5_000

        if (connection.responseCode != 200) {
            throw Exception("Spigot API returned a non-200 response code")
        }

        val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))

        val jsonObject = JSONObject(bufferedReader.readLines().joinToString(""))

        bufferedReader.close()
        return jsonObject.getString("current_version")
    }
}