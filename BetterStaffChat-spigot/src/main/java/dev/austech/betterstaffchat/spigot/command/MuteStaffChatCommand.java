/*
 * BetterStaffChat - MuteStaffChatCommand.java
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

import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MuteStaffChatCommand extends Command {
    public MuteStaffChatCommand(@NotNull String name, @NotNull String description, @NotNull List<String> aliases) {
        super(name, description, "", aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission("betterstaffchat.mutestaffchat")) {
            sender.sendMessage(TextUtil.colorize("&cNo permission."));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtil.colorize("&cThis command can only be used by players."));
            return true;
        }

        if (BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(((Player) sender).getUniqueId())) {
            BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().remove(((Player) sender).getUniqueId());
        } else {
            if (BetterStaffChatSpigot.getInstance().getToggledStaffChat().contains(((Player) sender).getUniqueId())) {
                BetterStaffChatSpigot.getInstance().getToggledStaffChat().remove(((Player) sender).getUniqueId());
            }
            BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().add(((Player) sender).getUniqueId());
        }

        sender.sendMessage(TextUtil.colorize(BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(((Player) sender).getUniqueId()) ?
                BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.mute-on") :
                        BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.mute-off"))
        );

        return true;
    }
}
