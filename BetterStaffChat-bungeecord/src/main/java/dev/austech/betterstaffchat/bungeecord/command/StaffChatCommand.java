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

package dev.austech.betterstaffchat.bungeecord.command;

import dev.austech.betterstaffchat.bungeecord.BetterStaffChatBungeeCord;
import dev.austech.betterstaffchat.bungeecord.util.StaffChatUtil;
import dev.austech.betterstaffchat.common.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class StaffChatCommand extends Command {
    public StaffChatCommand(String command, String permission, String... aliases) {
        super(command, permission, aliases);
        setPermissionMessage(TextUtil.colorize("&cNo permission."));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer && BetterStaffChatBungeeCord.getInstance().getIgnoreStaffChat().contains(((ProxiedPlayer) sender).getUniqueId())) {
            sender.sendMessage(TextUtil.colorizeToComponent("&cYour staff chat is currently disabled."));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(TextUtil.colorizeToComponent("&cUsage: /" + this.getName() + " <message...>"));
            return;
        }

        StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(sender, String.join(" ", Arrays.copyOfRange(args, 0, args.length))), "betterstaffchat.messages.read");

        if (sender instanceof ProxiedPlayer)
            StaffChatUtil.getInstance().discord(sender, ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-format")
                    .replace("%player_name%", sender.getName())
                    .replace("%message%", String.join(" ", Arrays.copyOfRange(args, 0, args.length)))
                    .replace("%server%", StaffChatUtil.getFormattedServerName(((ProxiedPlayer) sender).getServer().getInfo().getName())))));
        else
            StaffChatUtil.getInstance().discord(sender, ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-format")
                    .replace("%player_name%", BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-replacement"))
                    .replace("%message%", String.join(" ", Arrays.copyOfRange(args, 0, args.length)))
                    .replace("%server%", BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.console-server-replacement"))
            )));
    }
}
