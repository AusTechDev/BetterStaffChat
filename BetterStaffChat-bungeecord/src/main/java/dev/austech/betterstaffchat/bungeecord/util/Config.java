/*
 * BetterStaffChat - Config.java
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

import dev.austech.betterstaffchat.bungeecord.BetterStaffChatBungeeCord;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

@UtilityClass
public class Config {
    public void load() {
        BetterStaffChatBungeeCord.getInstance().getDataFolder().mkdir();

        File file = new File(BetterStaffChatBungeeCord.getInstance().getPluginDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = BetterStaffChatBungeeCord.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BetterStaffChatBungeeCord.getInstance().getPluginDataFolder(), "config.yml"));

            int currentVersion = ConfigurationProvider.getProvider(YamlConfiguration.class).load(BetterStaffChatBungeeCord.getInstance().getClass().getResourceAsStream("/config.yml")).getInt("config-version");

            if (config.getInt("config-version") != currentVersion)
                newVersion(config.getInt("config-version"));
            else
                BetterStaffChatBungeeCord.getInstance().setConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException exception) {
            File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            file.renameTo(broken);
            BetterStaffChatBungeeCord.getInstance().logPrefix("&cThe config file is broken, and has been renamed to config.yml.broken." + System.currentTimeMillis());

            try (InputStream in = BetterStaffChatBungeeCord.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BetterStaffChatBungeeCord.getInstance().setConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BetterStaffChatBungeeCord.getInstance().getPluginDataFolder(), "config.yml")));
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
    }

    public Optional<String> getOptional(String path) {
        String find = BetterStaffChatBungeeCord.getInstance().getConfig().getString(path);
        return Optional.ofNullable(find.isEmpty() ? null : find);
    }

    public void newVersion(int ver) throws IOException {
        File file = new File(BetterStaffChatBungeeCord.getInstance().getPluginDataFolder(), "config.yml");

        File broken = new File(file.getAbsolutePath() + ".old." + ver);
        file.renameTo(broken);
        BetterStaffChatBungeeCord.getInstance().logPrefix("&cThe config file is old, and has been renamed to config.yml.old." + ver);

        try (InputStream inputStream = BetterStaffChatBungeeCord.getInstance().getClass().getResourceAsStream("/config.yml")) {
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BetterStaffChatBungeeCord.getInstance().setConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BetterStaffChatBungeeCord.getInstance().getPluginDataFolder(), "config.yml")));
    }
}
