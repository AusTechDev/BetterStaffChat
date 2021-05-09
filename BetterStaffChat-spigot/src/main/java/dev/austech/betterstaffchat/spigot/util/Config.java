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

package dev.austech.betterstaffchat.spigot.util;

import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.common.util.UpdateChecker;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import jdk.nashorn.internal.runtime.ParserException;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@UtilityClass
public class Config {
    public void load() {
        BetterStaffChatSpigot.getInstance().getDataFolder().mkdir();

        File file = new File(BetterStaffChatSpigot.getInstance().getPluginDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream inputStream = BetterStaffChatSpigot.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(inputStream, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);

            if (config.getInt("config-version") != UpdateChecker.getCurrentConfigVersion())
                newVersion(config.getInt("config-version"));
            else
                BetterStaffChatSpigot.getInstance().setConfig(config);
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            file.renameTo(broken);
            BetterStaffChatSpigot.getInstance().logPrefix("&cThe config file is broken, and has been renamed to config.yml.broken." + System.currentTimeMillis());

            try (InputStream in = BetterStaffChatSpigot.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            BetterStaffChatSpigot.getInstance().setConfig(YamlConfiguration.loadConfiguration(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newVersion(int ver) throws IOException {
        File file = new File(BetterStaffChatSpigot.getInstance().getPluginDataFolder(), "config.yml");

        File broken = new File(file.getAbsolutePath() + ".old." + ver);
        file.renameTo(broken);
        BetterStaffChatSpigot.getInstance().logPrefix("&cThe config file is old, and has been renamed to config.yml.old." + ver);

        try (InputStream inputStream = BetterStaffChatSpigot.getInstance().getClass().getResourceAsStream("/config.yml")) {
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BetterStaffChatSpigot.getInstance().setConfig(YamlConfiguration.loadConfiguration(new File(BetterStaffChatSpigot.getInstance().getPluginDataFolder(), "config.yml")));
    }
}
