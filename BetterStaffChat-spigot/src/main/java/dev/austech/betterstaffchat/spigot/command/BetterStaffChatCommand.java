/*
 * BetterStaffChat - BetterStaffChatCommand.java
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
import dev.austech.betterstaffchat.common.discord.JDAImplementation;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BetterStaffChatCommand extends Command {
    public BetterStaffChatCommand() {
        super("betterstaffchat", "The main command for BetterStaffChat", "", Lists.newArrayList("bsc"));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            boolean reload = BetterStaffChatSpigot.getInstance().reloadConfig(sender);
            if (!reload && BetterStaffChatSpigot.getInstance().getConfig().getBoolean("discord.bot.enabled")) {
                ((JDAImplementation) BetterStaffChatSpigot.getInstance().getJda()).asJda().getPresence().setActivity(Activity.of(
                        Activity.ActivityType.valueOf(BetterStaffChatSpigot.getInstance().getConfig().getString("discord.bot.activity-type").toUpperCase().replace("PLAYING", "DEFAULT")),
                        BetterStaffChatSpigot.getInstance().getConfig().getString("discord.bot.activity")
                ));;
            }

            sender.sendMessage(TextUtil.colorize("&8&l&m-------------------------------"));
            sender.sendMessage(TextUtil.colorize(TextUtil.spacer(18) + "&c&l&oBetter&4&l&oStaffChat"));
            sender.sendMessage(TextUtil.colorize(TextUtil.spacer(13) + "&7Configuration Reloaded"));
            sender.sendMessage(TextUtil.colorize("&8&l&m-------------------------------"));
            return true;
        }

        sender.sendMessage(TextUtil.colorize("&8&l&m-------------------------------"));
        sender.sendMessage(TextUtil.colorize(TextUtil.spacer(18) + "&c&l&oBetter&4&l&oStaffChat"));
        sender.sendMessage(TextUtil.colorize(TextUtil.spacer(13) + "&7The &7&o\"better\"&r &7staff chat."));
        sender.sendMessage(TextUtil.colorize("&8&l&m-------------------------------"));
        return true;
    }
}
