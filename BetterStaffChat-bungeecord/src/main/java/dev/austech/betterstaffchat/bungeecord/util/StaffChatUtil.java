/*
 * BetterStaffChat - StaffChatUtil.java
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
import dev.austech.betterstaffchat.common.discord.DiscordWebhook;
import dev.austech.betterstaffchat.common.discord.JDAImplementation;
import dev.austech.betterstaffchat.common.util.AbstractStaffChatUtil;
import dev.austech.betterstaffchat.common.util.LogUtil;
import dev.austech.betterstaffchat.common.util.TextUtil;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.io.IOException;

public class StaffChatUtil extends AbstractStaffChatUtil {
    @Getter
    private static final StaffChatUtil instance = new StaffChatUtil();

    public static String getFormattedServerName(String serverName) {
        return Config.getOptional("staffchat.server-replacement." + serverName).orElse(serverName);
    }

    public String getFormattedMessage(Object sender, String message) {
        StringBuilder builder = new StringBuilder();
        message = BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.format")
                        .replace("%player_name%", (sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getName() : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-replacement"))
                        .replace("%message%", BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("staffchat.strip-color-codes") ? ChatColor.stripColor(TextUtil.colorize(message)) : message)
                        .replace("%server%",  (sender instanceof ProxiedPlayer) ? getFormattedServerName(((ProxiedPlayer) sender).getServer().getInfo().getName()) : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-server-replacement"));

        if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null && sender instanceof ProxiedPlayer) {
            message = message.replace("%luckperms_prefix%", LuckPermsUtil.getPrefix((ProxiedPlayer) sender)).replace("%luckperms_suffix%", LuckPermsUtil.getSuffix((ProxiedPlayer) sender));
        } else if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
            message = message.replace("%luckperms_prefix%", "").replace("%luckperms_suffix%", "");
        }

        for (String word : message.split(" ")) {
            builder.append(TextUtil.colorize(word));
            builder.append(" ");
        }

        return builder.substring(0, builder.length() - 1);
    }

    public void broadcast(String string, String permission) {
        for (ProxiedPlayer player : BetterStaffChatBungeeCord.getInstance().getProxy().getPlayers()) {
            if (!BetterStaffChatBungeeCord.getInstance().getIgnoreStaffChat().contains(player.getUniqueId()) && player.hasPermission(permission)) {
                player.sendMessage(new TextComponent(string));
            }
        }

        if (BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("staffchat.log-to-console")) {
            BetterStaffChatBungeeCord.getInstance().log(TextUtil.colorize(string));
        }
    }

    public void discord(Object sender, String string) {
        if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null && sender instanceof ProxiedPlayer) {
            string = string.replace("%luckperms_prefix%", ChatColor.stripColor(TextUtil.colorize(LuckPermsUtil.getPrefix((ProxiedPlayer) sender)))).replace("%luckperms_suffix%", ChatColor.stripColor(TextUtil.colorize(LuckPermsUtil.getSuffix((ProxiedPlayer) sender))));
        } else if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
            string = string.replace("%luckperms_prefix%", "").replace("%luckperms_suffix%", "");
        }

        String finalString = string;
        BetterStaffChatBungeeCord.getInstance().getProxy().getScheduler().runAsync(BetterStaffChatBungeeCord.getInstance(), () -> {
            if (BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("discord.webhook.enabled")) {
                DiscordWebhook webhook = new DiscordWebhook(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.webhook.url"));

                if (BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("discord.discord-messages.embed.enabled")) {
                    webhook.addEmbed(generateEmbed(sender, finalString));
                } else {
                    webhook.setContent(finalString);
                }

                try {
                    webhook.execute();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    LogUtil.logPrefix(BetterStaffChatBungeeCord.getInstance(), "&cFailed to send Discord webhook.");
                }
            }
            else if (BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("discord.bot.enabled")) {
                if (BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("discord.discord-messages.embed.enabled")) {
                    for (String guildChannelPair : BetterStaffChatBungeeCord.getInstance().getConfig().getStringList("discord.bot.channels")) {
                        String[] parts = guildChannelPair.split(": ");
                        ((JDAImplementation) BetterStaffChatBungeeCord.getInstance().getJda()).sendEmbed(parts[0], parts[1], generateEmbed(sender, finalString));
                    }
                } else {
                    for (String guildChannelPair : BetterStaffChatBungeeCord.getInstance().getConfig().getStringList("discord.bot.channels")) {
                        String[] parts = guildChannelPair.split(": ");
                        ((JDAImplementation) BetterStaffChatBungeeCord.getInstance().getJda()).sendMessage(parts[0], parts[1], finalString);
                    }
                }
            }
        });
    }

    @Override
    public void handleDiscord(Object event) {
        if (BetterStaffChatBungeeCord.getInstance().getConfig().getStringList("discord.bot.channels").stream().anyMatch(a -> a.equals(((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getGuild().getId() + ": " + ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getChannel().getId()))) {
            StringBuilder builder = new StringBuilder();

            StringBuilder discordMessage = new StringBuilder(((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getContentStripped().trim());

            if (!((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getAttachments().isEmpty()) {
                for (net.dv8tion.jda.api.entities.Message.Attachment attachment : ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getAttachments()) {
                    discordMessage.append(" ").append(attachment.getUrl());
                }
            }

            String message = BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.format")
                    .replace("%player_name%", ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getAuthor().getAsTag())
                    .replace("%message%", discordMessage.toString())
                    .replace("%server%", "Discord")
                    .replaceAll("%\\S*%", "");


            for (String word : message.split(" ")) {
                builder.append(ChatColor.translateAlternateColorCodes('&', word));
                builder.append(" ");
            }

            broadcast(builder.substring(0, builder.length() - 1), "betterstaffchat.messages.read");
        }
    }

    protected DiscordWebhook.EmbedObject generateEmbed(Object sender, String string) {
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setColor(Color.decode(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.embed.embed-color")));
        embed.setDescription(string);

        String footer = BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer")
                .replace("%player_name%", (sender instanceof ProxiedPlayer) ? TextUtil.cleanForDiscord(((ProxiedPlayer) sender).getName()) : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-replacement"))
                .replace("%uuid%", (sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getUniqueId().toString() : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-uuid-replacement"))
                .replace("%server%",  (sender instanceof ProxiedPlayer) ? getFormattedServerName(((ProxiedPlayer) sender).getServer().getInfo().getName()) : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-server-replacement"));

        String icon = BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer-icon").replace("%player_name%", (sender instanceof ProxiedPlayer) ? TextUtil.cleanForDiscord(((ProxiedPlayer) sender).getName()) : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-replacement"))
                .replace("%uuid%", (sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getUniqueId().toString() : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-uuid-replacement"))
                .replace("%server%",  (sender instanceof ProxiedPlayer) ? getFormattedServerName(((ProxiedPlayer) sender).getServer().getInfo().getName()) : BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-server-replacement"));

        if (!BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer").equals("")) {
            embed.setFooter(footer, icon);
        } else embed.setFooter(footer, null);
        return embed;
    }
}
