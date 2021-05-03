/*
 * BetterStaffChat - BetterStaffChatSpigot.java
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

package dev.austech.betterstaffchat.spigot;

import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.common.DependencyLoader;
import dev.austech.betterstaffchat.common.discord.JDAImplementation;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.command.BetterStaffChatCommand;
import dev.austech.betterstaffchat.spigot.command.MuteStaffChatCommand;
import dev.austech.betterstaffchat.spigot.command.StaffChatCommand;
import dev.austech.betterstaffchat.spigot.command.ToggleStaffChatCommand;
import dev.austech.betterstaffchat.spigot.listener.PlayerListener;
import dev.austech.betterstaffchat.spigot.reflection.ReflectionUtil;
import dev.austech.betterstaffchat.spigot.util.Config;
import dev.austech.betterstaffchat.spigot.util.Metrics;
import dev.austech.betterstaffchat.spigot.util.StaffChatUtil;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.ConfigurationException;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public final class BetterStaffChatSpigot extends JavaPlugin {

    @Getter private static BetterStaffChatSpigot instance;
    @Getter private final ArrayList<UUID> ignoreStaffChat = Lists.newArrayList();
    @Getter private final ArrayList<UUID> toggledStaffChat = Lists.newArrayList();
    @Getter private Object jda;
    @Getter boolean discordEnabled;
    @Getter @Setter private FileConfiguration config;
    @Getter private final int latestConfigVersion = 1;


    @Override
    public void onEnable() {
        instance = this;

        new Metrics(this, 10952);

        Config.load();

        if (getConfig().getBoolean("discord.bot.enabled") && getConfig().getBoolean("discord.webhook.enabled")) {
            new ConfigurationException("Both Discord types are enabled").printStackTrace();
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        this.discordEnabled = getConfig().getBoolean("discord.bot.enabled");

        DependencyLoader.init(this);

        if (discordEnabled) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    this.jda = new JDAImplementation(JDABuilder.createLight(getConfig().getString("discord.bot.token")).build(), StaffChatUtil.getInstance());
                    ((JDAImplementation) jda).asJda().getPresence().setActivity(Activity.of(
                            Activity.ActivityType.valueOf(getConfig().getString("discord.bot.activity-type").toUpperCase().replace("PLAYING", "DEFAULT")),
                            getConfig().getString("discord.bot.activity")
                    ));
                } catch (LoginException exception) {
                    exception.printStackTrace();
                    this.getPluginLoader().disablePlugin(this);
                }
            });
        }

        ReflectionUtil.registerCmd(new BetterStaffChatCommand());
        ReflectionUtil.registerCmd(new StaffChatCommand());
        ReflectionUtil.registerCmd(new ToggleStaffChatCommand());
        ReflectionUtil.registerCmd(new MuteStaffChatCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public boolean reloadConfig(CommandSender sender) {
        boolean discordLoaded = getConfig().getBoolean("discord.bot.enabled");
        Config.load();
        if (getConfig().getBoolean("discord.bot.enabled") != discordLoaded) {
            logPrefix(TextUtil.colorize("&cYou enabled the discord bot in the config. Please restart the server for changes to take effect."));
            return true;
        }
        return false;
    }

    public File getPluginDataFolder() {
        return getDataFolder();
    }

    public void log(String string) {
        Bukkit.getConsoleSender().sendMessage(TextUtil.colorize(string));
    }

    public void logPrefix(String string) {
        Bukkit.getConsoleSender().sendMessage("[BetterStaffChat] " + TextUtil.colorize(string));
    }

    public void logPrefixDebug(String string) {
        if (getConfig().getBoolean("debug")) Bukkit.getConsoleSender().sendMessage("[BetterStaffChat] Debug - " + TextUtil.colorize(string));
    }
}
