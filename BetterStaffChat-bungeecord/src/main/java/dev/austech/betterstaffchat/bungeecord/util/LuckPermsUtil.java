/*
 * BetterStaffChat - LuckPermsUtil.java
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

import dev.austech.betterstaffchat.common.util.TextUtil;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class LuckPermsUtil {
    @Setter
    private LuckPerms luckPerms;

    public String getPrefix(ProxiedPlayer player) {
        User lpUser = luckPerms.getUserManager().getUser(player.getUniqueId());
        if(lpUser.getCachedData().getMetaData().getPrefix() == null) return "";
        return TextUtil.colorize(lpUser.getCachedData().getMetaData().getPrefix());
    }

    public String getSuffix(ProxiedPlayer player) {
        User lpUser = luckPerms.getUserManager().getUser(player.getUniqueId());
        if(lpUser.getCachedData().getMetaData().getSuffix() == null) return "";
        return TextUtil.colorize(lpUser.getCachedData().getMetaData().getSuffix());
    }
}
