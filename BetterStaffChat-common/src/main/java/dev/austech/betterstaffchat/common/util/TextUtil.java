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

import dev.austech.betterstaffchat.common.plugin.Platform;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class TextUtil {
    private boolean rgbAvailable;
    private Platform platform;

    public void init(Platform serverPlatform, Object plugin) {
        platform = serverPlatform;
        if (serverPlatform.equals(Platform.BUKKIT)) {
            String packageName = plugin.getClass().getPackage().getName();
            if (Integer.parseInt(packageName.substring(packageName.lastIndexOf('.') + 1).split("_")[1]) >= 16)
                rgbAvailable = true;
        }
    }

    /**
     * Colors a specific message using the ChatColor API.
     * @param string input
     * @return Colored string
     */
    public String colorize(String string) {
        if (platform.equals(Platform.BUNGEECORD) || rgbAvailable)
            return ChatColor.translateAlternateColorCodes('&', colorizeRgb(string));
        else return ChatColor.translateAlternateColorCodes('&', string);
    }

    /* Pulled right from https://www.spigotmc.org/threads/hex-chat-class.449300/ */
    public String colorizeRgb(String string) {
        final Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})" + "");
        Matcher matcher = hexPattern.matcher(string);
        StringBuffer buffer = new StringBuffer(string.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public TextComponent colorizeToComponent(String string) {
        return new TextComponent(TextComponent.fromLegacyText(string));
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
