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

package dev.austech.betterstaffchat.bungeecord.command;

import dev.austech.betterstaffchat.bungeecord.BetterStaffChatBungeeCord;
import dev.austech.betterstaffchat.common.util.TextUtil;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BetterStaffChatCommand extends Command {
    public BetterStaffChatCommand() {
        super("betterstaffchat", null, "bsc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("betterstaffchat.reload")) {
            boolean reload = BetterStaffChatBungeeCord.getInstance().reloadConfig(sender);
            if (!reload && BetterStaffChatBungeeCord.getInstance().getConfig().getBoolean("discord.bot.enabled")) {
                BetterStaffChatBungeeCord.getInstance().getJda().asJda().getPresence().setActivity(Activity.of(
                        Activity.ActivityType.valueOf(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.bot.activity-type").toUpperCase().replace("PLAYING", "DEFAULT")),
                        BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.bot.activity")
                ));
            }

            sender.sendMessage(TextUtil.colorizeToComponent("&8&l&m-------------------------------"));
            sender.sendMessage(TextUtil.colorizeToComponent(TextUtil.spacer(18) + "&c&l&oBetter&4&l&oStaffChat"));
            sender.sendMessage(TextUtil.colorizeToComponent(TextUtil.spacer(13) + "&7Configuration Reloaded"));
            sender.sendMessage(TextUtil.colorizeToComponent("&8&l&m-------------------------------"));
            return;
        }

        sender.sendMessage(TextUtil.colorizeToComponent("&8&l&m-------------------------------"));
        sender.sendMessage(TextUtil.colorizeToComponent(TextUtil.spacer(18) + "&c&l&oBetter&4&l&oStaffChat"));
        sender.sendMessage(TextUtil.colorizeToComponent(TextUtil.spacer(13) + "&7The &7&o\"better\"&r &7staff chat."));
        sender.sendMessage(TextUtil.colorizeToComponent("&8&l&m-------------------------------"));
    }
}
