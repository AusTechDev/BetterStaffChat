/*
 * BetterStaffChat - TextUtil.java
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

package dev.austech.betterstaffchat.common.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

@UtilityClass
public final class TextUtil {

    /**
     * Colors a specific message using the ChatColor API.
     * @param string input
     * @return Colored string
     */
    public String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public TextComponent colorizeToComponent(String string) {
        return new TextComponent(colorize(string));
    }

    public String spacer(int amount) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < amount; i++)
            stringBuilder.append("&r ");
        return stringBuilder.toString();
    }

    public String cleanForDiscord(String string) {
        return string.replaceAll("/\\*/", "\\*").replaceAll("/\\//", "\\/");
    }
}
