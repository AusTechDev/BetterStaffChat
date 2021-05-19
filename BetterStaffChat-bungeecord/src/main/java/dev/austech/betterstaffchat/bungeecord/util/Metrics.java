/*
 * BetterStaffChat - Metrics.java
 * Copyright (C) 2021 AusTech Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.austech.betterstaffchat.bungeecord.util;

import dev.austech.betterstaffchat.common.metrics.MetricsBase;
import dev.austech.betterstaffchat.common.metrics.charts.CustomChart;
import dev.austech.betterstaffchat.common.metrics.json.JsonObjectBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class Metrics {

    private final Plugin plugin;
    private final MetricsBase metricsBase;

    private boolean enabled;
    private String serverUUID;
    private boolean logErrors = false;
    private boolean logSentData;
    private boolean logResponseStatusText;

    /**
     * Creates a new Metrics instance.
     *
     * @param plugin Your plugin instance.
     * @param serviceId The id of the service.
     *                  It can be found at <a href="https://bstats.org/what-is-my-plugin-id">What is my plugin id?</a>
     */
    public Metrics(Plugin plugin, int serviceId) {
        this.plugin = plugin;

        try {
            loadConfig();
        } catch (IOException e) {
            // Failed to load configuration
            plugin.getLogger().log(Level.WARNING, "Failed to load bStats config!", e);
            metricsBase = null;
            return;
        }

        metricsBase = new MetricsBase(
                "bungeecord",
                serverUUID,
                serviceId,
                enabled,
                this::appendPlatformData,
                this::appendServiceData,
                null,
                () -> true,
                (message, error) -> this.plugin.getLogger().log(Level.WARNING, message, error),
                (message) -> this.plugin.getLogger().log(Level.INFO, message),
                logErrors,
                logSentData,
                logResponseStatusText
        );
    }

    /**
     * Loads the bStats configuration.
     */
    private void loadConfig() throws IOException {
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        bStatsFolder.mkdirs();
        File configFile = new File(bStatsFolder, "config.yml");
        if (!configFile.exists()) {
            writeFile(configFile,
                    "# bStats (https://bStats.org) collects some basic information for plugin authors, like how",
                    "# many people use their plugin and their total player count. It's recommended to keep bStats",
                    "# enabled, but if you're not comfortable with this, you can turn this setting off. There is no",
                    "# performance penalty associated with having metrics enabled, and data sent to bStats is fully",
                    "# anonymous.",
                    "enabled: true",
                    "serverUuid: \"" + UUID.randomUUID() + "\"",
                    "logFailedRequests: false",
                    "logSentData: false",
                    "logResponseStatusText: false");
        }

        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

        // Load configuration
        enabled = configuration.getBoolean("enabled", true);
        serverUUID = configuration.getString("serverUuid");
        logErrors = configuration.getBoolean("logFailedRequests", false);
        logSentData = configuration.getBoolean("logSentData", false);
        logResponseStatusText = configuration.getBoolean("logResponseStatusText", false);
    }

    private void writeFile(File file, String... lines) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(CustomChart chart) {
        metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount",  plugin.getProxy().getOnlineCount());
        builder.appendField("managedServers", plugin.getProxy().getServers().size());
        builder.appendField("onlineMode", plugin.getProxy().getConfig().isOnlineMode() ? 1 : 0);
        builder.appendField("bungeecordVersion", plugin.getProxy().getVersion());

        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", plugin.getDescription().getVersion());
    }

}