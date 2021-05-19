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

package dev.austech.betterstaffchat.spigot.util;

import dev.austech.betterstaffchat.common.discord.DiscordWebhook;
import dev.austech.betterstaffchat.common.discord.JDAImplementation;
import dev.austech.betterstaffchat.common.util.AbstractStaffChatUtil;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.common.util.LogUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class StaffChatUtil extends AbstractStaffChatUtil {
    @Getter private static final StaffChatUtil instance = new StaffChatUtil();

    public String getFormattedMessage(Object sender, String message) {
        message = TextUtil.colorize(
                BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.format")
                        .replace("%player_name%", (sender instanceof Player) ? ((Player) sender).getName() : BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-replacement"))
                        .replace("%message%", BetterStaffChatSpigot.getInstance().getConfig().getBoolean("staffchat.strip-color-codes") ? ChatColor.stripColor(TextUtil.colorize(message)) : message)
        );

        if (sender instanceof Player) {
            message = TextUtil.colorize(placeholder((Player) sender, message));
        }
        return message;
    }
    
    public String placeholder(Player player, String string) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, string);
        } else return string;
    }


    /**
     * An alternative to {@link Bukkit#broadcast} with support for staffchat toggle and Discord
     */
    public void broadcast(String string, String permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(player.getUniqueId()) && player.hasPermission(permission)) {
                player.sendMessage(string);
            }
        }

        if (BetterStaffChatSpigot.getInstance().getConfig().getBoolean("staffchat.log-to-console")) {
            Bukkit.getConsoleSender().sendMessage(TextUtil.colorize(string));
        }
    }

    public void discord(Object sender, String string) {
        if (sender instanceof Player) string = placeholder((Player) sender, string);
        string = ChatColor.stripColor(TextUtil.colorize(string));
        String finalString = string;
        Bukkit.getScheduler().runTaskAsynchronously(BetterStaffChatSpigot.getInstance(), () -> {

            if (BetterStaffChatSpigot.getInstance().getConfig().getBoolean("discord.webhook.enabled")) {
                DiscordWebhook webhook = new DiscordWebhook(BetterStaffChatSpigot.getInstance().getConfig().getString("discord.webhook.url"));

                if (BetterStaffChatSpigot.getInstance().getConfig().getBoolean("discord.discord-messages.embed.enabled")) {
                    webhook.addEmbed(generateEmbed(sender, finalString));
                } else {
                    webhook.setContent(finalString);
                }

                try {
                    webhook.execute();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    LogUtil.log(BetterStaffChatSpigot.getInstance(), "&cFailed to send Discord webhook.");
                }
            }
            else if (BetterStaffChatSpigot.getInstance().getConfig().getBoolean("discord.bot.enabled")) {
                if (BetterStaffChatSpigot.getInstance().getConfig().getBoolean("discord.discord-messages.embed.enabled")) {
                    for (String guildChannelPair : BetterStaffChatSpigot.getInstance().getConfig().getStringList("discord.bot.channels")) {
                        String[] parts = guildChannelPair.split(": ");
                        ((JDAImplementation) BetterStaffChatSpigot.getInstance().getJda()).sendEmbed(parts[0], parts[1], generateEmbed(sender, finalString));
                    }
                } else {
                    for (String guildChannelPair : BetterStaffChatSpigot.getInstance().getConfig().getStringList("discord.bot.channels")) {
                        String[] parts = guildChannelPair.split(": ");
                        ((JDAImplementation) BetterStaffChatSpigot.getInstance().getJda()).sendMessage(parts[0], parts[1], finalString);
                    }
                }
            }
        });
    }

    @Override
    public void handleDiscord(Object event) {
        if (BetterStaffChatSpigot.getInstance().getConfig().getStringList("discord.bot.channels").stream().anyMatch(a -> a.equals(((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getGuild().getId() + ": " + ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getChannel().getId()))) {
            StringBuilder builder = new StringBuilder();

            StringBuilder discordMessage = new StringBuilder(((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getContentStripped().trim());

            if (!((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getAttachments().isEmpty()) {
                for (net.dv8tion.jda.api.entities.Message.Attachment attachment : ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMessage().getAttachments()) {
                    discordMessage.append(" ").append(attachment.getUrl());
                }
            }

            String nickName = ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getMember().getNickname();

            String name = BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.name-format")
                    .replace("%username%", ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getAuthor().getName())
                    .replace("%discriminator%", ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getAuthor().getDiscriminator())
                    .replace("%nickname%", nickName != null ? nickName : ((net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent) event).getAuthor().getName());

            String message = BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.format")
                    .replace("%player_name%", name)
                    .replace("%message%", discordMessage.toString())
                    .replace("%server%",  "Discord")
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
        embed.setColor(Color.decode(BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.embed.embed-color")));
        embed.setDescription(string);

        String footer = BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer")
                .replace("%player_name%", (sender instanceof Player) ? TextUtil.cleanForDiscord(((Player) sender).getName()) : BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-replacement"))
                .replace("%uuid%", (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-uuid-replacement"));
        if (sender instanceof Player) footer = ChatColor.stripColor(TextUtil.colorize(placeholder(((Player) sender), footer)));

        String icon = BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer-icon").replace("%player_name%", (sender instanceof Player) ? TextUtil.cleanForDiscord(((Player) sender).getName()) : BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-replacement"))
                .replace("%uuid%", (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-uuid-replacement"));
        if (sender instanceof Player) icon = ChatColor.stripColor(TextUtil.colorize(placeholder(((Player) sender), icon)));

        if (!BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.embed.embed-footer").equals("")) {
            embed.setFooter(footer, icon);
        } else embed.setFooter(footer, null);
        return embed;
    }
}
