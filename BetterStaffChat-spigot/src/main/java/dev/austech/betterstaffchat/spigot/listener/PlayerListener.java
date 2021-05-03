/*
 * BetterStaffChat - PlayerListener.java
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

package dev.austech.betterstaffchat.spigot.listener;

import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import dev.austech.betterstaffchat.spigot.util.StaffChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String prefix = BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.prefix");

        if (event.getPlayer().hasPermission("betterstaffchat.messages.send") && event.getMessage().startsWith(prefix) && event.getMessage().length() > prefix.length() && !prefix.equals("")) {
            event.setCancelled(true);

            if (BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(TextUtil.colorize("&cYour staff chat is currently disabled."));
                return;
            }

            StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(event.getPlayer(), event.getMessage().substring(prefix.length())), "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-format").replace("%player_name%", event.getPlayer().getName()).replace("%message%", event.getMessage().substring(prefix.length()))))));
        } else if (event.getPlayer().hasPermission("betterstaffchat.messages.send") && BetterStaffChatSpigot.getInstance().getToggledStaffChat().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);

            StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(event.getPlayer(), event.getMessage()), "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-format").replace("%player_name%", event.getPlayer().getName()).replace("%message%", event.getMessage())))));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.join") && !BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.join").equals("")) {
            String message = TextUtil.colorize(
                    BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.join")
                            .replace("%player_name%", event.getPlayer().getName())
            );

            message = TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), message));

            StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-join").replace("%player_name%", event.getPlayer().getName())))));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.leave") && !BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.leave").equals("")) {
            String message = TextUtil.colorize(
                    BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.leave")
                            .replace("%player_name%", event.getPlayer().getName())
            );

            message = TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), message));

            StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-leave").replace("%player_name%", event.getPlayer().getName())))));
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.switch") && !BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.switch").equals("")) {
            String message = TextUtil.colorize(
                    BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.switch")
                            .replace("%player_name%", event.getPlayer().getName())
                            .replace("%from%", event.getFrom().getName())
                            .replace("%to%", event.getPlayer().getWorld().getName())
            );

            message = TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), message));

            StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder(event.getPlayer(), BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-switch")
                    .replace("%player_name%", event.getPlayer().getName())
                    .replace("%from%", event.getFrom().getName())
                    .replace("%to%", event.getPlayer().getWorld().getName())))));
        }
    }
}
