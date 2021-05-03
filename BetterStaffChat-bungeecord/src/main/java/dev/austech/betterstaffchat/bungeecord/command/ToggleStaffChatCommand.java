/*
 * BetterStaffChat - ToggleStaffChatCommand.java
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
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleStaffChatCommand extends Command {
    public ToggleStaffChatCommand() {
        super("togglestaffchat", "betterstaffchat.togglestaffchat", "tsc", "sct", "staffchattoggle");
        setPermissionMessage(TextUtil.colorize("&cNo permission."));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextUtil.colorizeToComponent("&cThis command can only be used by players."));
            return;
        }

        if (sender instanceof ProxiedPlayer && BetterStaffChatBungeeCord.getInstance().getIgnoreStaffChat().contains(((ProxiedPlayer) sender).getUniqueId())) {
            sender.sendMessage(TextUtil.colorizeToComponent("&cYour staff chat is currently disabled."));
            return;
        }

        if (BetterStaffChatBungeeCord.getInstance().getToggledStaffChat().contains(((ProxiedPlayer) sender).getUniqueId())) {
            BetterStaffChatBungeeCord.getInstance().getToggledStaffChat().remove(((ProxiedPlayer) sender).getUniqueId());
        } else {
            BetterStaffChatBungeeCord.getInstance().getToggledStaffChat().add(((ProxiedPlayer) sender).getUniqueId());
        }

        sender.sendMessage(TextUtil.colorizeToComponent(BetterStaffChatBungeeCord.getInstance().getToggledStaffChat().contains(((ProxiedPlayer) sender).getUniqueId()) ?
                BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.toggle-on") :
                BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.toggle-off"))
        );
    }
}
