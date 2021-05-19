/*
 * BetterStaffChat - StaffChatCommand.java
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

package dev.austech.betterstaffchat.spigot.command;

import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import dev.austech.betterstaffchat.spigot.util.StaffChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class StaffChatCommand extends Command {
    public StaffChatCommand(@NotNull String name, @NotNull String description, @NotNull List<String> aliases) {
        super(name, description, "", aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission("betterstaffchat.messages.send")) {
            sender.sendMessage(TextUtil.colorize("&cNo permission."));
            return true;
        }

        if (sender instanceof Player && BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(((Player) sender).getUniqueId())) {
            sender.sendMessage(TextUtil.colorize("&cYour staff chat is currently disabled."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(TextUtil.colorize("&cUsage: /" + commandLabel + " " + this.getUsage()));
            return true;
        }

        StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(sender, String.join(" ", Arrays.copyOfRange(args, 0, args.length))), "betterstaffchat.messages.read");

        if (sender instanceof Player)
            StaffChatUtil.getInstance().discord(sender, ChatColor.stripColor(TextUtil.colorize(StaffChatUtil.getInstance().placeholder((Player) sender, BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-format").replace("%player_name%", sender.getName()).replace("%message%", String.join(" ", Arrays.copyOfRange(args, 0, args.length)))))));
        else
            StaffChatUtil.getInstance().discord(sender, ChatColor.stripColor(TextUtil.colorize(BetterStaffChatSpigot.getInstance().getConfig().getString("discord.discord-messages.staffchat-format").replace("%player_name%",
                    BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.console-replacement")).replace("%message%", String.join(" ", Arrays.copyOfRange(args, 0, args.length))))));

        return true;
    }
}
