/*
 * BetterStaffChat - BetterStaffChatBungeeCord.java
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

package dev.austech.betterstaffchat.bungeecord;

import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.bungeecord.command.BetterStaffChatCommand;
import dev.austech.betterstaffchat.bungeecord.command.MuteStaffChatCommand;
import dev.austech.betterstaffchat.bungeecord.command.StaffChatCommand;
import dev.austech.betterstaffchat.bungeecord.command.ToggleStaffChatCommand;
import dev.austech.betterstaffchat.bungeecord.listener.PlayerListener;
import dev.austech.betterstaffchat.bungeecord.util.Config;
import dev.austech.betterstaffchat.bungeecord.util.LuckPermsUtil;
import dev.austech.betterstaffchat.bungeecord.util.Metrics;
import dev.austech.betterstaffchat.bungeecord.util.StaffChatUtil;
import dev.austech.betterstaffchat.common.DependencyLoader;
import dev.austech.betterstaffchat.common.discord.JDAImplementation;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.common.util.UpdateChecker;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import javax.naming.ConfigurationException;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class BetterStaffChatBungeeCord extends Plugin {
    @Getter private static BetterStaffChatBungeeCord instance;
    @Getter @Setter private Configuration config;
    @Getter private final ArrayList<UUID> ignoreStaffChat = Lists.newArrayList();
    @Getter private final ArrayList<UUID> toggledStaffChat = Lists.newArrayList();
    @Getter private JDAImplementation jda;
    @Getter boolean discordEnabled;

    @Override
    public void onEnable() {
        instance = this;

        TextUtil.init(false, this);
        Config.load();

        if (getConfig().getBoolean("check-for-updates"))
            getProxy().getScheduler().runAsync(this, () -> {
                if (UpdateChecker.needsUpdate(this, getDescription().getVersion())) {
                    getProxy().getScheduler().schedule(this, () -> {
                        logPrefix("&eA new update for BetterStaffChat is available...");
                        logPrefix("&ehttps://www.spigotmc.org/resources/91991");
                    }, 3, TimeUnit.SECONDS);
                }
            });

        new Metrics(this, 10954);

        if (getConfig().getBoolean("discord.bot.enabled") && getConfig().getBoolean("discord.webhook.enabled")) {
            new ConfigurationException("Both Discord types are enabled").printStackTrace();
            return;
        }

        if (this.getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPermsUtil.setLuckPerms(net.luckperms.api.LuckPermsProvider.get());
        }

        this.discordEnabled = getConfig().getBoolean("discord.bot.enabled");

        DependencyLoader.init(this);

        if (discordEnabled) {
            this.getProxy().getScheduler().runAsync(this, () -> {
                try {
                    this.jda = new JDAImplementation(net.dv8tion.jda.api.JDABuilder.createLight(getConfig().getString("discord.bot.token")).build(), StaffChatUtil.getInstance());
                    ((JDAImplementation) jda).asJda().getPresence().setActivity(net.dv8tion.jda.api.entities.Activity.of(
                            net.dv8tion.jda.api.entities.Activity.ActivityType.valueOf(getConfig().getString("discord.bot.activity-type").toUpperCase().replace("PLAYING", "DEFAULT")),
                            getConfig().getString("discord.bot.activity")
                    ));
                } catch (LoginException exception) {
                    exception.printStackTrace();
                    return;
                }
            });
        }

        this.getProxy().getPluginManager().registerListener(this, new PlayerListener());
        this.getProxy().getPluginManager().registerCommand(this, new BetterStaffChatCommand());
        this.getProxy().getPluginManager().registerCommand(this, new MuteStaffChatCommand());
        this.getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ToggleStaffChatCommand());
    }

    public File getPluginDataFolder() {
        return getDataFolder();
    }

    public void logPrefix(String string) {
        getProxy().getConsole().sendMessage(new TextComponent("[BetterStaffChat] " + TextUtil.colorize(string)));
    }

    public void logPrefixDebug(String string) {
        if (getConfig().getBoolean("debug")) getProxy().getConsole().sendMessage(new TextComponent("[BetterStaffChat] Debug - " + TextUtil.colorize(string)));
    }

    public void log(String string) {
        getProxy().getConsole().sendMessage(new TextComponent(TextUtil.colorize(string)));
    }

    public boolean reloadConfig(CommandSender sender) {
        boolean discordLoaded = getConfig().getBoolean("discord.bot.enabled");
        Config.load();
        if (getConfig().getBoolean("discord.bot.enabled") != discordLoaded) {
            logPrefix("&cYou enabled the discord bot in the config. Please restart the server for changes to take effect.");
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        if (isDiscordEnabled()) (getJda()).shutdown();
    }
}
